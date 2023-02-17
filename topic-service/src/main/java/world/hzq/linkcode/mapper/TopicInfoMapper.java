package world.hzq.linkcode.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import world.hzq.linkcode.entity.Solution;
import world.hzq.linkcode.entity.Topic;
import world.hzq.linkcode.entity.TopicInput;
import world.hzq.linkcode.entity.TopicOutput;

import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description 题目信息mapper
 * @date 2023/2/5 04:33
 */
@Mapper
public interface TopicInfoMapper {

    List<TopicInput> getTopicInputListById(Long id);

    List<TopicOutput> getTopicOutputList(List<Long> topicInputIdList);

    List<Topic> getTopicInfo(@Param("start") Integer start, @Param("count") Integer count);

    List<Solution> getTopicSolutionList(@Param("topicId") Long topicId, @Param("languageType") String languageType);

    Topic getTopic(Long id);

    Topic getTopicInfoByTopicName(@Param("topicName") String topicName);

    List<Long> getTopicInputIdList(Long id);

    Long getTopicCount();

    String getTopicStatus(@Param("userId") Long userId,@Param("topicId") Long topicId);
}
