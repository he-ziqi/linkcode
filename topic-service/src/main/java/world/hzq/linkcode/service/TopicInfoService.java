package world.hzq.linkcode.service;

import world.hzq.linkcode.entity.Solution;
import world.hzq.linkcode.entity.Topic;
import world.hzq.linkcode.entity.TopicInput;
import world.hzq.linkcode.entity.TopicOutput;
import world.hzq.linkcode.util.Result;
import world.hzq.linkcode.vo.InfoVO;

import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description 题目信息接口
 * @date 2023/2/5 04:29
 */
public interface TopicInfoService {
    /**
     * @methodName 获取题目信息
     * @param: id 题目id
     * @return: world.hzq.sjm.util.Result<world.hzq.sjm.entity.Topic>
     * @author hzq
     * @date 2023/2/3 01:45
     */
    Result<Topic> getTopicInfo(Long id);

    /**
     * @description 获取输入集列表
     * @param: id 题目id
     * @return: world.hzq.sjm.util.Result<java.util.List<world.hzq.sjm.entity.TopicInput>>
     * @author hzq
     * @date 2023/2/3 14:59
     */
    Result<List<TopicInput>> getTopicInputList(Long id);

    /**
     * @description 通过输入集id获取题目输出集
     * @param: topicInputIdList 输入集id集合
     * @return: world.hzq.sjm.util.Result<java.util.List<world.hzq.sjm.entity.TopicOutput>>
     * @author hzq
     * @date 2023/2/3 15:10
     */
    Result<List<TopicOutput>> getTopicOutputList(List<Long> topicInputIdList);

    /**
     * @description 分页获取题目信息
     * @param: pageNo 页码
     * @param: pageSize 页大小
     * @return: world.hzq.sjm.util.Result<java.util.List<world.hzq.sjm.entity.Topic>>
     * @author hzq
     * @date 2023/2/3 16:29
     */
    Result<InfoVO<Topic>> getTopicInfo(Integer pageNo, Integer pageSize);

    /**
     * @description 获取题解信息
     * @param topicId 题目id
     * @param languageType 语言类型
     * @return world.hzq.sjm.util.Result<java.util.List<world.hzq.sjm.entity.Solution>>
     * @author hzq
     * @date 2023/2/3 17:35
     */
    Result<InfoVO<Solution>> getSolutionList(Long topicId, String languageType,Integer pageNo,Integer pageSize);

    /**
     * @description 通过题目名称或者题目描述分页获取题目 (查询es)
     * @param: topicName 题目名称
     * @param: topicComment 题目内容
     * @param pageNo 页码
     * @param pageSize 分页大小
     * @param difficulty 0表示难度升序排序,1表示降序排序
     * @author hzq
     * @date 2023/2/5 04:49
     */
    Result<InfoVO<Topic>> getTopicByNameOrComment(String topicName, String topicComment, Integer pageNo, Integer pageSize, Integer difficulty);

    /**
     * @description 获取题目状态码表
     * @return: world.hzq.sjm.util.Result<java.lang.String>
     * @author hzq
     * @date 2023/2/11 01:13
     */
    Result<List<String>> getTopicStatusCodeTable();

}
