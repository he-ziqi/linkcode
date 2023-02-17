package world.hzq.linkcode.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import world.hzq.linkcode.entity.ClassRoom;
import world.hzq.linkcode.vo.ApplyRecordVO;

import java.util.Date;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/12 17:11
 */
@Mapper
public interface ClassRoomInfoManagementMapper {

    Integer createClassRoom(ClassRoom classRoom);

    ClassRoom getClassRoomByTeacherIdAndClassRoomId(@Param("classRoomId") Long classRoomId,
                                                    @Param("userId") Long userId);

    Integer joinClassRoom(@Param("classRoomId") Long classRoomId,@Param("studentId") Long studentId,
                          @Param("joinTime") Date joinTime,@Param("nickName") String nickName);

    Integer updateClassRoomNumberIncrement(@Param("classRoomId") Long classRoomId,
                                           @Param("number") Integer number);

    ClassRoom getClassRoomById(Long classRoomId);

    Long getTeacherIdByClassRoomId(Long classRoomId);

    Integer addApplyRecord(@Param("applyTime") Date applyTime,@Param("timeout") Date timeout,
                           @Param("classRoomId") Long classRoomId,@Param("teacherId") Long teacherId,
                           @Param("studentId") Long studentId,@Param("nickName") String nickName);

    Integer addClassRoomTeacherRelation(@Param("classRoomId") Long classRoomId,
                                        @Param("teacherId") Long teacherId);

    ApplyRecordVO getApplyRecordById(Long recordId);

    Integer updateApplyRecord(@Param("recordId") Long recordId,@Param("operationType") String operationType);

    Integer deleteClassRoomStudentRelationByClassRoomId(Long classRoomId);

    Integer deleteClassRoomTeacherRelationByClassRoomId(Long classRoomId);

    Integer deleteClassRoomApplyRecordByClassRoomId(Long classRoomId);

    Integer deleteClassRoomById(Long classRoomId);

    Integer deleteClassRoomApplyRecordByClassRoomIdAndStudentId(@Param("studentId") Long studentId,
                                                                @Param("classRoomId") Long classRoomId);

    Integer deleteClassRoomStudentRelationByClassRoomIdAndStudentId(@Param("studentId") Long studentId,
                                                                    @Param("classRoomId") Long classRoomId);

}
