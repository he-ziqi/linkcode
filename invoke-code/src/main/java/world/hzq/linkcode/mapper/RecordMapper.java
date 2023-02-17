package world.hzq.linkcode.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import world.hzq.linkcode.entity.SolutionRecord;


/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/9 22:47
 */
@Mapper
public interface RecordMapper {
    Integer saveRecord(SolutionRecord solutionRecord);

    Integer insertTopicStatus(@Param("userId") Long userId,@Param("topicId") Long topicId,@Param("status") String status);

    String existsTopicStatus(@Param("userId") Long userId,@Param("topicId") Long topicId);


    Integer updateTopicStatus(@Param("userId") Long userId,@Param("topicId") Long topicId,@Param("status") String status);
}
