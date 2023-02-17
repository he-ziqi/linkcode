package world.hzq.linkcode.service;

import world.hzq.linkcode.dto.AddTopicDTO;
import world.hzq.linkcode.entity.Solution;
import world.hzq.linkcode.entity.TopicInput;
import world.hzq.linkcode.entity.TopicOutput;
import world.hzq.linkcode.util.Result;

import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/2 22:36
 */
public interface TopicManagementService {
    /**
     * @methodName 添加题目
     * @description 添加题目信息、输入集信息、题解信息添加到数据库,输出集信息为懒加载暂不存入数据库(避免分布式事务出现)
     * @param: topicDTO
     * @return: world.hzq.sjm.util.Result<java.lang.String>
     * @author hzq
     * @date 2023/2/2 22:37
     */
    Result<String> addTopic(AddTopicDTO topicDTO) throws Exception;

    /**
     * @methodName 通过题目id删除题目
     * @param: id 题目id
     * @return: world.hzq.sjm.util.Result<java.lang.String>
     * @author hzq
     * @date 2023/2/3 01:57
     */
    Result<String> removeTopic(Long id);

    /**
     * @description 更新题目信息
     * @param: topicDTO 题目信息传输对象
     * @return: world.hzq.sjm.util.Result<java.lang.String>
     * @author hzq
     * @date 2023/2/3 02:32
     */
    Result<String> updateTopic(AddTopicDTO topicDTO);

    /**
     * @description 增加题目输入集,需要将原有的输出集全部删除,待执行代码时重新生成输出集
     * @param: topicInputList 输入集对象列表
     * @return: world.hzq.sjm.util.Result<java.lang.String>
     * @author hzq
     * @date 2023/2/3 03:36
     */
    Result<String> addTopicInput(List<TopicInput> topicInputList);

    /**
     * @description 增加题解
     * @param: topicSolutionList 题解列表
     * @return: world.hzq.sjm.util.Result<java.lang.String>
     * @author hzq
     * @date 2023/2/3 03:37
     */
    Result<String> addTopicSolution(List<Solution> topicSolutionList);

    /**
     * @description 增加输出集内容
     * @param: outputList 输出集列表
     * @return: world.hzq.sjm.util.Result<java.lang.String>
     * @author hzq
     * @date 2023/2/3 14:50
     */
    Result<String> addTopicOutput(List<TopicOutput> outputList);

}
