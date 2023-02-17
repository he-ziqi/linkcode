package world.hzq.linkcode.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import world.hzq.linkcode.entity.*;

import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/14 00:38
 */
@Mapper
public interface JobInfoMapper {
    Long getChoiceJobInfoCount(@Param("teacherId") Long teacherId,
                               @Param("classRoomId") Long classRoomId);

    Long getProgramJobInfoCount(@Param("teacherId") Long teacherId,
                                @Param("classRoomId") Long classRoomId);
    
    User getTeacherById(Long teacherId);

    ClassRoom getClassRoomById(Long classRoomId);

    List<ChoiceTopic> getChoiceJobByTeacherIdAndClassRoomId(@Param("teacherId") Long teacherId,
                                                            @Param("classRoomId") Long classRoomId,
                                                            @Param("start") Integer start,
                                                            @Param("pageSize") Integer pageSize);

    List<ProgramTopic> getProgramJobByTeacherIdAndClassRoomId(@Param("teacherId") Long teacherId,
                                                              @Param("classRoomId") Long classRoomId,
                                                              @Param("start") Integer start,
                                                              @Param("pageSize") Integer pageSize);

    Long getTeacherByClassRoomId(Long classRoomId);


    String getTopicCommitStatus(@Param("jobId") Long jobId,
                                @Param("type") String type,
                                @Param("studentId") Long studentId,
                                @Param("classRoomId") Long classRoomId);

    void updateChoiceTopicStatus(String status);

    void updateProgramTopicStatus(String status);
}
