package world.hzq.linkcode.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import world.hzq.linkcode.entity.*;
import world.hzq.linkcode.exception.CompileException;
import world.hzq.linkcode.feign.TopicOperationFeign;
import world.hzq.linkcode.mapper.RecordMapper;
import world.hzq.linkcode.mq.MessageQueueInfo;
import world.hzq.linkcode.mq.producer.InvokeRecordSaveProducer;
import world.hzq.linkcode.type.InvokeRecordStatusType;
import world.hzq.linkcode.type.TopicStatusType;
import world.hzq.linkcode.util.ResultState;
import world.hzq.linkcode.util.Tools;
import world.hzq.linkcode.dynamiccompilation.*;
import world.hzq.linkcode.service.CodeInvokeService;
import world.hzq.linkcode.util.Result;
import world.hzq.linkcode.util.WebUtil;
import world.hzq.linkcode.vo.InfoVO;
import world.hzq.linkcode.vo.InvokeResult;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;


/**
 * @author hzq
 * @version 1.0
 * @description 代码执行实现
 * @date 2023/2/2 17:34
 */
@Service
@Slf4j
public class CodeInvokeServiceImpl implements CodeInvokeService {

    @Autowired
    private TopicOperationFeign topicOperationFeign;

    @Autowired
    private WebUtil webUtil;

    @Resource
    private RecordMapper recordMapper;

    @Autowired
    private InvokeRecordSaveProducer invokeRecordSaveProducer;

    /**
     * @description 获取编程语言类型
     * @param: languageType
     * @return: world.hzq.sjm.dynamiccompilation.LanguageType
     * @author hzq
     * @date 2023/2/9 22:08
     */
    public LanguageType getLanguageType(String languageType){
        LanguageType codeType = null;
        for (LanguageType type : LanguageType.values()) {
            if(Tools.equals(type.getCode(),languageType)){
                codeType = type;
                break;
            }
        }
        return codeType;
    }

    /**
     * @description 初始化输出集合
     * @param: topicId 题目id
     * @param: languageType 语言类型
     * @param: executionEngine 执行引擎
     * @param: inputList 输入集
     * @param: timeout 超时时间
     * @return: world.hzq.sjm.util.Result<java.util.List<world.hzq.sjm.entity.TopicOutput>>
     * @author hzq
     * @date 2023/2/9 22:21
     */
    public Result<List<TopicOutput>> initOutputCollection(Long topicId,String languageType,ExecutionEngine executionEngine,List<TopicInput> inputList,Integer timeout){
        //获取正确代码
        Result<InfoVO<Solution>> solutionResult = topicOperationFeign.getSolutionList(topicId, languageType, 1, 1);
        if(!Tools.responseOk(solutionResult.getCode())){ //非正常响应
            return Result.error("获取题解出错,系统异常");
        }
        //不存在题解 直接返回
        if(solutionResult.getData().getSource().size() == 0){
            return Result.success("暂无题解,请试试其它题目哦");
        }
        //获取正确代码(默认使用第一个)
        String answerCode = solutionResult.getData().getSource().get(0).getAnswerCode();
        //定义输出集集合
        List<TopicOutput> outputList = new ArrayList<>(inputList.size());
        //定义正确输出集合
        List<TopicOutput> correctOutputList = new ArrayList<>(inputList.size());
        //获取输入集
        for (TopicInput topicInput : inputList) {
            //执行正确代码,对输出集进行初始化 默认时间单位为毫秒
            String invokeResult = null;
            try {
                invokeResult = executionEngine.invoke(answerCode, timeout, TimeUnit.MILLISECONDS, topicInput.getInputContent());
            } catch (CompileException e) {
                //编译出现异常
                e.printStackTrace();
                return Result.error("题解编译出错,系统异常,msg:" + e.getMessage());
            }
            //获取结果字符串
            String resultString = invokeResult.substring(0, invokeResult.lastIndexOf(CompilationConstraintsEnum.TIME_SPENT.getCode()));
            //将执行结果添加到输出集集合中
            outputList.add(new TopicOutput(0L, resultString.trim(), topicInput.getId()));
            //将执行结果添加到正确输出集合中
            correctOutputList.add(new TopicOutput(0L, resultString.trim(), topicInput.getId()));
        }

        //将结果增加到输出集中
        Result<String> addTopicOutputResult = topicOperationFeign.addTopicOutput(outputList);
        if(!Tools.responseOk(addTopicOutputResult.getCode())){ //非正常响应
            return Result.error("添加输出集出错,系统异常");
        }
        return Result.success(correctOutputList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<InvokeResult> invokeCode(String code, String languageType, Long topicId){
        //如果用户登录 则初始设置用户状态为未通过
        User user = webUtil.currentUser();
        if(Tools.isNotNull(user)){
            //查询此题目记录是否存在
            String status = recordMapper.existsTopicStatus(user.getId(),topicId);
            if(Tools.isNull(status)){
                //不存在
                Integer updated = recordMapper.insertTopicStatus(user.getId(),topicId,TopicStatusType.TOPIC_STATUS_NOT_PASS.getCode());
                log.info("用户:{},更新题目:{},状态为not_pass,成功:{}",user.getId(),topicId,updated == 1);
            }else{
                Integer updated = recordMapper.updateTopicStatus(user.getId(),topicId,TopicStatusType.TOPIC_STATUS_NOT_PASS.getCode());
                log.info("用户:{},更新题目:{},初始重置为not_pass,成功:{}",user.getId(),topicId,updated == 1);
            }
        }
        //获取编程语言类型
        LanguageType codeType = getLanguageType(languageType);
        if(Tools.isNull(codeType)){
            return Result.error(CompilationConstraintsEnum.NOT_SUPPORT_CODE.getValue() + languageType);
        }
        //获取题目输入集和正确代码
        Result<List<TopicInput>> inputResult = topicOperationFeign.getTopicInput(topicId);
        if(!Tools.responseOk(inputResult.getCode())){ //非正常响应
            return Result.error("执行出错,系统异常");
        }
        //获取执行引擎
        ExecutionEngine executionEngine = ExecutionFactory.getExecutionEngineByType(codeType);
        //定义正确输出集合
        List<TopicOutput> correctOutputList = null;
        //获取输入集
        List<TopicInput> inputList = inputResult.getData();
        //获取输入集id列表
        List<Long> inputIdList = new ArrayList<>(inputList.size());
        inputList.forEach(topicInput -> inputIdList.add(topicInput.getId()));
        //获取题目信息
        Result<Topic> topicInfo = topicOperationFeign.getTopicInfo(topicId);
        //获取输出集
        Result<List<TopicOutput>> topicOutputResult = topicOperationFeign.getTopicOutput(inputIdList);
        if(!Tools.responseOk(topicOutputResult.getCode())){ //非正常响应
            return Result.error("获取输出集出错,系统异常");
        }else{
            //输出集未初始化
            if(topicOutputResult.getData().size() == 0){
                //初始化输出集并返回
                Result<List<TopicOutput>> result = initOutputCollection(topicId, languageType, executionEngine, inputList, topicInfo.getData().getTimeout());
                if(!Tools.responseOk(result.getCode())){
                    //未成功响应则出现异常 直接结束
                    return Result.error(result.getMsg());
                }
                //获取正确输出集
                correctOutputList = result.getData();
            }else { //输出集已初始化
                //从题目服务中获取输出集合正确输出集合
                correctOutputList = topicOutputResult.getData();
            }
            if(correctOutputList.size() != inputList.size()){
                return Result.error("输入集与输出集数量不匹配,系统异常");
            }
        }
        if(correctOutputList.size() == 0){
            //输出集不存在
            return Result.fail("执行出错,经初始化和获取后输出集仍不存在");
        }
        //记录当前用例是否ac
        Boolean[] accept = new Boolean[inputList.size()];
        //默认填充为false
        Arrays.fill(accept,false);
        int acCount = 0;
        //用户代码执行输出内容集合
        List<String> userOutput = new ArrayList<>(inputList.size());
        long timeSpent = 0L; //执行耗时
        //获取预期输出集合
        List<String> expectedOutput = new ArrayList<>(inputList.size());
        //使用输入集作为输入 执行用户代码
        for (int i = 0; i < inputList.size(); i++) {
            //获取当前输入集对象
            TopicInput topicInput = inputList.get(i);
            //获取输入内容
            String inputContent = topicInput.getInputContent();

            String invokeResultString = null;
            try {
                invokeResultString = executionEngine.invoke(code, topicInfo.getData().getTimeout(), TimeUnit.MILLISECONDS, inputContent);
            } catch (CompileException e) {
                //编译出现异常
                e.printStackTrace();
                return Result.fail("exception msg : " + e.getMessage());
            }

            AtomicReference<TopicOutput> currentTopicOut = new AtomicReference<>();
            //获取对应输出集
            for (TopicOutput topicOutput : correctOutputList) {
                //找到对应的输出集
                if (topicOutput.getTopicInputId().equals(topicInput.getId())) {
                    currentTopicOut.set(topicOutput);
                    break;
                }
            }
            //获取用户代码执行的结果字符串
            int index = invokeResultString.lastIndexOf(CompilationConstraintsEnum.TIME_SPENT.getCode());
            //执行超时
            if(Tools.equals(CompilationConstraintsEnum.MESSAGE_TIME_OUT.getValue(),invokeResultString)){
                return Result.fail(CompilationConstraintsEnum.MESSAGE_TIME_OUT.getValue());
            }
            String resultString = invokeResultString.substring(0,index);
            //当前执行时长
            long currentTimeSpent = Long.parseLong(invokeResultString.substring(index + CompilationConstraintsEnum.TIME_SPENT.getCode().length() + 1));
            timeSpent += currentTimeSpent; //累计每次执行耗时
            //将用户执行结果添加到用户代码执行输出详情集合中
            userOutput.add(resultString.trim());
            //将正确输出结果添加到预期输出集合中
            String correctResult = currentTopicOut.get().getOutputContent().trim();
            expectedOutput.add(correctResult);
            //比对执行结果是否一致
            if(Tools.equals(resultString.trim(),correctResult)){
                accept[i] = true; //当前用例已ac
                acCount++; //通过用例数+1
            }
        }
        //执行超时
        if(timeSpent > topicInfo.getData().getTimeout()){
            return Result.fail(CompilationConstraintsEnum.MESSAGE_TIME_OUT.getValue());
        }
        //获取输入集输入内容详情集合
        List<String> inputContentList = new ArrayList<>(inputList.size());
        inputList.forEach(topicInput -> inputContentList.add(topicInput.getInputContent()));
        //封装执行结果
        InvokeResult invokeResult = new InvokeResult(acCount,inputList.size(),accept,acCount == inputList.size(),userOutput,inputContentList,expectedOutput,timeSpent);
        //通过则修改题目状态未通过
        if(Tools.isNotNull(user) && acCount == inputList.size()){
            Integer updated = recordMapper.updateTopicStatus(user.getId(),topicId,TopicStatusType.TOPIC_STATUS_PASS.getCode());
            log.info("用户:{},更新题目:{},状态为pass,成功:{}",user.getId(),topicId,updated == 1);
        }
        //返回结果
        return Result.success("执行成功",invokeResult);
    }

    @Override
    public Result<List<String>> getSupportLanguage() {
        List<String> languageTypeList = new ArrayList<>();
        for (LanguageType languageType : LanguageType.values()) {
            languageTypeList.add(languageType.getCode());
        }
        return Result.success(languageTypeList);
    }

    //无论是任何情况 已登录的用户都保存执行记录
    @Override
    public Result<String> saveRecord(String code, String languageType, Long topicId, Result<InvokeResult> invokeResult) {
        //已登录用户保存执行记录
        User user = webUtil.currentUser();
        if(Tools.isNull(user)){
            return Result.fail("游客模式,不保存执行记录");
        }
        SolutionRecord solutionRecord = new SolutionRecord();
        solutionRecord.setCodeContent(code);
        solutionRecord.setLanguageType(languageType);
        solutionRecord.setTopicId(topicId);
        solutionRecord.setUpdateTime(new Date());
        solutionRecord.setUserId(user.getId());
        solutionRecord.setAcceptCount(0);
        solutionRecord.setInvokeTime(-1L);
        String topicStatusType;
        String msg = null;
        //执行异常
        if(invokeResult.getCode() == ResultState.FAIL.getCode()){
            solutionRecord.setInvokeTime(-1L);
            //执行异常
            if(invokeResult.getMsg().startsWith("exception msg : ")){
                topicStatusType = InvokeRecordStatusType.INVOKE_EXCEPTION_TYPE.getCode();
                //异常信息
                msg = invokeResult.getMsg();
            }else {
                topicStatusType = InvokeRecordStatusType.INVOKE_TIME_LIMIT_TYPE.getCode();
                //执行超时
                msg = CompilationConstraintsEnum.MESSAGE_TIME_OUT.getValue();
            }
        }else if(invokeResult.getCode() == ResultState.ERROR.getCode()){ //系统异常
            topicStatusType = InvokeRecordStatusType.INVOKE_ERROR_TYPE.getCode();
            msg = "系统异常";
            solutionRecord.setInvokeTime(-1L);
        }else {
            //正常执行
            if (invokeResult.getData().getIsAccept()) {
                topicStatusType = InvokeRecordStatusType.INVOKE_PASS_TYPE.getCode();
            } else {
                topicStatusType = InvokeRecordStatusType.INVOKE_NOT_PASS_TYPE.getCode();
            }
        }
        solutionRecord.setTopicStatusType(topicStatusType);
        //正常执行
        if(msg == null){
            msg = "执行成功,通过用例:" + invokeResult.getData().getAcCount() + "/" + invokeResult.getData().getTotal();
            solutionRecord.setAcceptCount(invokeResult.getData().getAcCount());
            solutionRecord.setInvokeTime(invokeResult.getData().getTimeSpent());
        }
        solutionRecord.setMsg(msg);
        //保存执行记录
        Integer inserted = recordMapper.saveRecord(solutionRecord);
        log.info("用户:{},保存执行记录:{} 保存结果:{}",user,solutionRecord,inserted > 0);
        //通知mq插入用户执行记录
        invokeRecordSaveProducer.sendMessage(MessageQueueInfo.TopicTypeInfo.ADD_INVOKE_RECORD_TOPIC,solutionRecord);
        return inserted > 0 ? Result.success("保存成功") : Result.fail("保存失败");
    }
}
