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
 * @description TODO
 * @date 2023/2/2 22:39
 */
@Mapper
public interface TopicManagementMapper {

    Integer insertTopicInfo(@Param("topic") Topic topic);

    Integer insertTopicInputList(List<TopicInput> topicInput);

    Integer insertTopicSolutionList(List<Solution> solution);

    Integer removeTopicInfo(Long id);

    Integer removeTopicInput(Long id);

    Integer removeTopicOutput(List<Long> idList);

    Integer removeTopicSolution(Long id);

    Integer updateTopicInfo(@Param("topic") Topic topic);

    Integer updateTopicSolution(List<Solution> solution);

    Integer insertTopicOutput(List<TopicOutput> outputList);

}
