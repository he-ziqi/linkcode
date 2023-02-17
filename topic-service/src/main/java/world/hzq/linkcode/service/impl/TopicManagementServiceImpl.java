package world.hzq.linkcode.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import world.hzq.linkcode.dto.AddTopicDTO;
import world.hzq.linkcode.entity.Solution;
import world.hzq.linkcode.entity.Topic;
import world.hzq.linkcode.entity.TopicInput;
import world.hzq.linkcode.entity.TopicOutput;
import world.hzq.linkcode.mapper.TopicInfoMapper;
import world.hzq.linkcode.mapper.TopicManagementMapper;
import world.hzq.linkcode.mq.MessageQueueInfo;
import world.hzq.linkcode.mq.producer.TopicInfoProducer;
import world.hzq.linkcode.service.TopicManagementService;
import world.hzq.linkcode.util.Result;
import world.hzq.linkcode.util.Tools;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/2 22:38
 */
@Service
@Slf4j
public class TopicManagementServiceImpl implements TopicManagementService {
    private static final Logger logger = LoggerFactory.getLogger(TopicManagementServiceImpl.class);

    //题目管理操作mapper
    @Resource
    private TopicManagementMapper topicManagementMapper;

    //题目信息mapper
    @Resource
    private TopicInfoMapper topicInfoMapper;

    //题目信息生产者
    @Resource
    private TopicInfoProducer topicInfoProducer;

    //遇到所有异常都回滚
    @Override
    @Transactional(rollbackFor = Exception.class,isolation = Isolation.READ_UNCOMMITTED)
    public Result<String> addTopic(AddTopicDTO topicDTO) throws Exception{

        if(Tools.isNull(topicDTO.getTopic()) || Tools.isNull(topicDTO.getTopicInput()) || topicDTO.getTopicInput().size() == 0){
            return Result.fail("题目信息和输入集信息不能为空");
        }
        //查询题目是否存在
        Topic topic = topicInfoMapper.getTopicInfoByTopicName(topicDTO.getTopic().getTopicName());
        if(topic != null){
            return Result.fail("该题目已经存在,不能重复添加");
        }
        //1、将题目信息存入数据库
        Integer insertTopicInfoCount = topicManagementMapper.insertTopicInfo(topicDTO.getTopic());
        Long topicId = topicDTO.getTopic().getId();
        topicDTO.getTopicInput().forEach(topicInput -> topicInput.setTopicId(topicId));
        topicDTO.getSolution().forEach(solution -> solution.setTopicId(topicId));
        //2、将题目输入集存入数据库
        Integer insertTopicInputCount = topicManagementMapper.insertTopicInputList(topicDTO.getTopicInput());
        //3、将题解信息存入数据库中
        if(topicDTO.getSolution().size() > 0){
            //设置添加题解的时间为当前时间
            Date now = new Date();
            topicDTO.getSolution().forEach(solution -> solution.setCreateTime(now));
            Integer insertTopicSolutionCount = topicManagementMapper.insertTopicSolutionList(topicDTO.getSolution());
            if(insertTopicSolutionCount <= 0){
                return Result.fail("题解信息添加失败");
            }
        }
        //发送mq消息添加es数据
        Topic topicInfo = topicDTO.getTopic();
        logger.info("生产者准备发送添加题目消息,题目信息：{}",topicInfo);
        topicInfoProducer.sendMessage(MessageQueueInfo.TopicTypeInfo.ADD_TOPIC_INFO_TOPIC,topicInfo);
        topicDTO.getSolution().forEach(solution -> {
            logger.info("生产者准备发送添加题解消息,题解信息：{}",solution);
            topicInfoProducer.sendMessage(MessageQueueInfo.TopicTypeInfo.ADD_TOPIC_SOLUTION_INFO_TOPIC,solution);
        });

        return insertTopicInfoCount + insertTopicInputCount >= 2
                ? Result.success("添加成功")
                : Result.fail("添加失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> removeTopic(Long id){
        //获取输入集id集合
        List<Long> inputIdList = topicInfoMapper.getTopicInputIdList(id);
        //删除输入集信息
        Integer deleteTopicInputCount = topicManagementMapper.removeTopicInput(id);
        //删除输出集信息
        //通过输入集的id集合删除输出集
        Integer deleteTopicOutCount = topicManagementMapper.removeTopicOutput(inputIdList);
        //删除题解信息
        Integer deleteTopicSolutionCount = topicManagementMapper.removeTopicSolution(id);
        //删除题目信息(有外键引用 题目信息必须最后删除)
        Integer deleteTopicInfoCount = topicManagementMapper.removeTopicInfo(id);

        //发送mq 删除题目信息和题解信息(通过topicId删除)
        topicInfoProducer.sendMessage(MessageQueueInfo.TopicTypeInfo.DELETE_TOPIC_INFO_ALL_TOPIC,id);

        //至少包含题目信息一条记录、输入集信息一条记录、题解信息一条记录(无题解也至少一条)
        return deleteTopicInfoCount + deleteTopicInputCount + deleteTopicOutCount + deleteTopicSolutionCount >= 3
                ? Result.success("删除成功") : Result.fail("删除失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> updateTopic(AddTopicDTO topicDTO) {
        Topic topic = topicInfoMapper.getTopicInfoByTopicName(topicDTO.getTopic().getTopicName());
        if(topic == null){
            return Result.fail("当前题目不存在,请先添加");
        }
        //不需要改动输入集、输出集、题解
        //更新题目信息
        Integer count = topicManagementMapper.updateTopicInfo(topicDTO.getTopic());
        if(count <= 0){
            logger.info("题目信息更新失败");
            return Result.fail("题目信息更新失败");
        }
        //更新题解信息
        if(topicDTO.getSolution().size() != 0){
            Integer updateSolutionCount = topicManagementMapper.updateTopicSolution(topicDTO.getSolution());
            if(updateSolutionCount <= 0){
                logger.info("题解信息更新失败");
                return Result.fail("题解信息更新失败");
            }
            //发送更新题解信息的消息
            topicInfoProducer.sendMessage(MessageQueueInfo.TopicTypeInfo.UPDATE_TOPIC_SOLUTION_INFO_TOPIC,topicDTO.getSolution());
        }
        //mq同步更新题目信息
        topicInfoProducer.sendMessage(MessageQueueInfo.TopicTypeInfo.UPDATE_TOPIC_INFO_TOPIC,topicDTO.getTopic());

        //需要将输入集和输出集重新设置
        if (topicDTO.getTopicInput().size() != 0) {
            //获取输入集id集合
            List<Long> inputIdList = topicInfoMapper.getTopicInputIdList(topicDTO.getTopic().getId());
            //删除输出集
            Integer deleteTopicOutCount = topicManagementMapper.removeTopicOutput(inputIdList);
            //删除输入集
            Integer deleteTopicInputCount = topicManagementMapper.removeTopicInput(topicDTO.getTopic().getId());
            //新增输入集
            topicManagementMapper.insertTopicInputList(topicDTO.getTopicInput());
            //输出集为懒加载 暂不更新
            //记录行数应大于等于新加入的输入集记录数
            return deleteTopicInputCount + deleteTopicOutCount >= topicDTO.getTopicInput().size()
                    ? Result.success("更新成功") : Result.fail("更新失败");
        }
        return Result.success("更新成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> addTopicInput(List<TopicInput> topicInputList) {
        if (topicInputList.size() == 0) {
            return Result.fail("输入集不能为空");
        }
        Topic topic = topicInfoMapper.getTopic(topicInputList.get(0).getTopicId());
        if(topic == null){
            return Result.fail("题目不存在,请先添加题目");
        }
        //通过题目id获取全部的输入集id集合
        Long topicId = topicInputList.get(0).getTopicId();
        List<Long> inputIdList = topicInfoMapper.getTopicInputIdList(topicId);
        //删除输出集内容
        Integer removeCount = topicManagementMapper.removeTopicOutput(inputIdList);
        if(removeCount == 0){
            return Result.fail("输入集添加失败,在删除原有输出集时出错");
        }
        //添加输入集内容
        return topicManagementMapper.insertTopicInputList(topicInputList)
                >= topicInputList.size()
                ? Result.success("输入集添加成功")
                : Result.fail("输入集添加失败");
    }

    @Override
    public Result<String> addTopicSolution(List<Solution> topicSolutionList) {
        if (topicSolutionList.size() == 0) {
            return Result.fail("题解不能为空");
        }
        Long topicId = topicSolutionList.get(0).getTopicId();
        Topic topic = topicInfoMapper.getTopic(topicId);
        if(topic == null){
            return Result.fail("题目不存在,请先添加题目");
        }
        Date now = new Date();
        topicSolutionList.forEach(solution -> solution.setCreateTime(now));
        //发送mq消息添加es数据
        topicSolutionList.forEach(solution -> {
            topicInfoProducer.sendMessage(MessageQueueInfo.TopicTypeInfo.ADD_TOPIC_SOLUTION_INFO_TOPIC,solution);
        });

        return topicManagementMapper.insertTopicSolutionList(topicSolutionList)
                >= topicSolutionList.size()
                ? Result.success("题解添加成功")
                : Result.fail("题解添加失败");
    }

    @Override
    public Result<String> addTopicOutput(List<TopicOutput> outputList) {
        if (outputList.size() == 0) {
            return Result.fail("添加失败,输出集不能为空");
        }
        return topicManagementMapper.insertTopicOutput(outputList) >= outputList.size()
                ? Result.success("添加成功")
                : Result.fail("添加失败");
    }

}
