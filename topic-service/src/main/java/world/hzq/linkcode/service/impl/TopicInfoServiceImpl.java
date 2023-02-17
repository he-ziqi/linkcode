package world.hzq.linkcode.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import world.hzq.linkcode.entity.*;
import world.hzq.linkcode.es.TopicInfoElasticSearchOperationExecutor;
import world.hzq.linkcode.es.TopicSolutionElasticSearchOperationExecutor;
import world.hzq.linkcode.exception.ElasticSearchOperationException;
import world.hzq.linkcode.mapper.TopicInfoMapper;
import world.hzq.linkcode.service.TopicInfoService;
import world.hzq.linkcode.type.TopicStatusType;
import world.hzq.linkcode.util.Result;
import world.hzq.linkcode.util.Tools;
import world.hzq.linkcode.util.WebUtil;
import world.hzq.linkcode.vo.InfoVO;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/5 04:30
 */
@Service
@Slf4j
public class TopicInfoServiceImpl implements TopicInfoService {

    private static final Logger logger = LoggerFactory.getLogger(TopicInfoServiceImpl.class);

    @Resource
    private TopicInfoMapper topicInfoMapper;

    @Autowired
    private WebUtil webUtil;

    @Autowired
    private TopicSolutionElasticSearchOperationExecutor topicSolutionElasticSearchOperationExecutor;

    @Autowired
    private TopicInfoElasticSearchOperationExecutor topicInfoElasticSearchOperationExecutor;

    @Override
    public Result<List<TopicInput>> getTopicInputList(Long id) {
        List<TopicInput> inputList = topicInfoMapper.getTopicInputListById(id);
        return inputList.size() > 0
                ? Result.success("获取成功",inputList)
                : Result.fail("获取输入集失败,输入集为null");
    }

    @Override
    public Result<List<TopicOutput>> getTopicOutputList(List<Long> topicInputIdList) {
        List<TopicOutput> outputList = topicInfoMapper.getTopicOutputList(topicInputIdList);
        return Result.success("获取成功",outputList);
    }

    @Override
    public Result<InfoVO<Topic>> getTopicInfo(Integer pageNo, Integer pageSize) {
        //获取题目总数量
        Long total = topicInfoMapper.getTopicCount();
        List<Topic> topicList = topicInfoMapper.getTopicInfo((pageNo - 1) * pageSize,pageSize);
        InfoVO<Topic> topicInfoVO = new InfoVO<>(topicList,total);
        //获取当前用户的题目状态
        User user = webUtil.currentUser();
        if(user != null){
            topicList.forEach(topic -> {
                String status = topicInfoMapper.getTopicStatus(user.getId(), topic.getId());
                if(Tools.isNotNull(status)){
                    topic.setStatus(status);
                }
            });
        }
        return Result.success("获取成功",topicInfoVO);
    }

    @Override
    public Result<InfoVO<Solution>> getSolutionList(Long topicId, String languageType,Integer pageNo,Integer pageSize) {
        InfoVO<Solution> solutionInfoVO = null;
        try {
            solutionInfoVO = topicSolutionElasticSearchOperationExecutor.getTopicSolutionInfo(topicId,languageType,pageNo,pageSize);
        } catch (ElasticSearchOperationException e) {
            return Result.error("查询失败,系统异常");
        }
        return Result.success("获取成功",solutionInfoVO);
    }

    //查询es获取题目信息
    @Override
    public Result<InfoVO<Topic>> getTopicByNameOrComment(String topicName, String topicComment, Integer pageNo, Integer pageSize, Integer difficulty) {
        InfoVO<Topic> topicInfoVO = null;
        try {
            topicInfoVO = topicInfoElasticSearchOperationExecutor.getTopicInfo(topicName,topicComment,pageNo,pageSize,difficulty);
        } catch (ElasticSearchOperationException e) {
            e.printStackTrace();
            return Result.error("查询失败,系统异常");
        }
        User user = webUtil.currentUser();
        if(Tools.isNotNull(user)){
            List<Topic> topicList = topicInfoVO.getSource();
            topicList.forEach(topic -> {
                String status = topicInfoMapper.getTopicStatus(user.getId(), topic.getId());
                if(Tools.isNotNull(status)){
                    topic.setStatus(status);
                }
            });
        }
        return Result.success(topicInfoVO);
    }

    @Override
    public Result<List<String>> getTopicStatusCodeTable() {
        List<String> types = new ArrayList<>(TopicStatusType.values().length);
        for (TopicStatusType type : TopicStatusType.values()) {
            types.add(type.getCode());
        }
        return Result.success(types);
    }

    @Override
    public Result<Topic> getTopicInfo(Long id) {
        Topic topic = topicInfoMapper.getTopic(id);
        if(Tools.isNull(topic)){
            return Result.fail("找不到该题目");
        }
        User user = webUtil.currentUser();
        //用户存在则查询题目状态
        if(Tools.isNotNull(user)){
            Long userId = user.getId();
            String status = topicInfoMapper.getTopicStatus(userId,id);
            topic.setStatus(status);
        }
        return Result.success(topic);
    }
}
