package world.hzq.linkcode.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import world.hzq.linkcode.entity.User;

import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/16 16:45
 */
@Mapper
public interface JobDataStatisticsMapper {

    List<User> getStudentBySubmitStatusAndAccepted(@Param("classRoomId") Long classRoomId,
                                                   @Param("choiceTopicId") Long choiceTopicId,
                                                   @Param("status") String status,
                                                   @Param("accepted") Integer accepted,
                                                   @Param("type") String type);

    String getStudentNickNameById(@Param("studentId") Long studentId,
                                 @Param("classRoomId") Long classRoomId);
}
