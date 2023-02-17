package world.hzq.linkcode.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import world.hzq.linkcode.dto.AddChoiceTopicDTO;
import world.hzq.linkcode.dto.AddProgramingTopicDTO;
import world.hzq.linkcode.dto.SubmitJobDTO;
import world.hzq.linkcode.feign.InvokeCodeFeign;
import world.hzq.linkcode.mapper.JobManagementMapper;
import world.hzq.linkcode.service.JobManagementService;
import world.hzq.linkcode.type.JobCommitStatusType;
import world.hzq.linkcode.type.TopicType;
import world.hzq.linkcode.util.Result;
import world.hzq.linkcode.util.TimeUtil;
import world.hzq.linkcode.util.Tools;
import world.hzq.linkcode.util.WebUtil;
import world.hzq.linkcode.vo.InvokeResult;
import world.hzq.linkcode.vo.TopicSubmitResultVO;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author hzq
 * @version 1.0
 * @description 作业管理服务实现
 * @date 2023/2/13 20:17
 */
@Service
public class JobManagementServiceImpl implements JobManagementService {

    @Resource
    private JobManagementMapper jobManagementMapper;

    @Autowired
    private WebUtil webUtil;

    @Autowired
    private InvokeCodeFeign invokeCodeFeign;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> releaseChoiceJob(AddChoiceTopicDTO addChoiceTopicDTO) {
        //获取班级的所有学生id
        List<Long> studentIdList = jobManagementMapper.getClassRoomStudentIdList(addChoiceTopicDTO.getClassRoomId());
        if(studentIdList.size() == 0){
            return Result.fail("班级还没有学生,不能发布作业");
        }
        //添加选择题到选择题表中
        Integer inserted1 = jobManagementMapper.addChoiceTopic(addChoiceTopicDTO);
        //初始化所有学生的选择题提交记录到作业提交表中
        Integer inserted2 = jobManagementMapper.addTopicCommitRecord(
                addChoiceTopicDTO.getId(), //选择题主键
                TopicType.TOPIC_TYPE_CHOICE.getCode(), //题目类型
                studentIdList, //学生id列表
                JobCommitStatusType.JOB_STATUS_UNCOMMIT.getCode(), //作业提交状态
                addChoiceTopicDTO.getClassRoomId() //班级id
        );
        return inserted1 + inserted2 >= 2 ? Result.success("发布成功") : Result.fail("发布失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> bulkReleaseChoiceJob(List<AddChoiceTopicDTO> addChoiceTopicDTOList) {
        List<Long> studentIdList = jobManagementMapper.getClassRoomStudentIdList(addChoiceTopicDTOList.get(0).getClassRoomId());
        if(studentIdList.size() == 0){
            return Result.fail("班级还没有学生,不能发布作业");
        }
        Integer inserted1 = jobManagementMapper.bulkAddChoiceTopic(addChoiceTopicDTOList);
        AtomicReference<Integer> inserted2 = new AtomicReference<>(0);
        //批量添加记录到选择题提交表中
        addChoiceTopicDTOList.forEach(addChoiceTopicDTO -> {
            inserted2.updateAndGet(v -> v +
                    jobManagementMapper.addTopicCommitRecord(
                        addChoiceTopicDTO.getId(),
                        TopicType.TOPIC_TYPE_CHOICE.getCode(),
                        studentIdList,
                        JobCommitStatusType.JOB_STATUS_UNCOMMIT.getCode(),
                        addChoiceTopicDTO.getClassRoomId()
                    )
            );
        });
        return Result.success("发布" + addChoiceTopicDTOList.size() + "条,成功：" + (inserted1 + inserted2.get()) + " 条,失败：" + (addChoiceTopicDTOList.size() - inserted1 - inserted2.get()) + "条" );
    }

    @Override
    public Result<String> bulkReleaseProgram(List<AddProgramingTopicDTO> addProgramingTopicDTOList) {
        List<Long> studentIdList = jobManagementMapper.getClassRoomStudentIdList(addProgramingTopicDTOList.get(0).getClassRoomId());
        if(studentIdList.size() == 0){
            return Result.fail("班级还没有学生,不能发布作业");
        }
        Integer inserted1 = jobManagementMapper.bulkAddProgramTopic(addProgramingTopicDTOList);
        AtomicReference<Integer> inserted2 = new AtomicReference<>(0);
        //批量添加记录到作业提交表中
        addProgramingTopicDTOList.forEach(addProgramingTopicDTO -> {
            inserted2.updateAndGet(v -> v +
                    jobManagementMapper.addTopicCommitRecord(
                        addProgramingTopicDTO.getId(),
                        TopicType.TOPIC_TYPE_PROGRAM.getCode(),
                        studentIdList,
                        JobCommitStatusType.JOB_STATUS_UNCOMMIT.getCode(),
                        addProgramingTopicDTO.getClassRoomId()
                    )
            );
        });
        return Result.success("发布" + addProgramingTopicDTOList.size() + "条,成功：" + (inserted1 + inserted2.get()) + " 条,失败：" + (addProgramingTopicDTOList.size() - inserted1 - inserted2.get()) + "条" );
    }

    @Override
    public Result<TopicSubmitResultVO> submitJob(SubmitJobDTO submitJobDTO) {
        //查询要提交的作业的截止提交时间
        Date stopTime = null;
        if (Tools.equals(TopicType.TOPIC_TYPE_CHOICE.getCode(), submitJobDTO.getType())) {
            stopTime = jobManagementMapper.getChoiceTopicStopTime(submitJobDTO.getJobId());
        }else if(Tools.equals(TopicType.TOPIC_TYPE_PROGRAM.getCode(), submitJobDTO.getType())){
            stopTime = jobManagementMapper.getProgramTopicStopTime(submitJobDTO.getJobId());
        }else{
            return Result.fail("非法作业类型");
        }
        if(Tools.isNull(stopTime)){
            return Result.fail("此作业不存在,无法提交");
        }
        Date commitTime = new Date();
        if(!TimeUtil.thanTime(stopTime,commitTime)){
            return Result.fail("此作业已截止提交");
        }
        TopicSubmitResultVO resultVO = new TopicSubmitResultVO();
        //提交题目是否通过
        short ac = 0;
        //获取该题目答案
        if(Tools.equals(TopicType.TOPIC_TYPE_CHOICE.getCode(), submitJobDTO.getType())){
            String answer = jobManagementMapper.getChoiceTopicAnswer(submitJobDTO.getJobId());
            if(Tools.equals(answer,submitJobDTO.getContent())){
                //答案正确
                ac = 1;
                resultVO.setIsAccepted(true);
            }else {
                resultVO.setIsAccepted(false);
            }
            resultVO.setTopicType(TopicType.TOPIC_TYPE_CHOICE.getCode());
        }else{
            Long topicId = submitJobDTO.getJobId();
            //通过topicId获取真正的编程题id
            Long programTopicId = jobManagementMapper.getRealProgramTopicId(topicId);
            String code = submitJobDTO.getContent();
            String languageType = submitJobDTO.getLanguageType();
            //远程调用执行代码
            Result<InvokeResult> result = invokeCodeFeign.executeCode(code, languageType, programTopicId);
            resultVO.setTopicType(TopicType.TOPIC_TYPE_PROGRAM.getCode());
            if(Tools.responseOk(result.getCode())){
                //是否通过
                Boolean isAccept = result.getData().getIsAccept();
                resultVO.setIsAccepted(isAccept);
                ac = 1;
            }else{
                resultVO.setIsAccepted(false);
            }
            resultVO.setInvokeResult(result.getData());
        }
        //将作业信息更新到作业提交表中
        Integer inserted = jobManagementMapper.updateJobCommitRecord(
                submitJobDTO,
                webUtil.currentUser().getId(),
                JobCommitStatusType.JOB_STATUS_COMMIT.getCode(),
                commitTime,
                ac
        );
        return inserted == 1 ? Result.success("作业提交成功",resultVO) : Result.fail("作业提交失败");
    }
}
