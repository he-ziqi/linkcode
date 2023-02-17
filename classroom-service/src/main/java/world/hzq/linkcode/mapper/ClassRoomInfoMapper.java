package world.hzq.linkcode.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import world.hzq.linkcode.entity.ClassRoom;
import world.hzq.linkcode.entity.User;
import world.hzq.linkcode.vo.ApplyRecordVO;
import world.hzq.linkcode.vo.ClassStudentVO;

import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description 课堂信息获取mapper
 * @date 2023/2/12 23:41
 */
@Mapper
public interface ClassRoomInfoMapper {
    List<ApplyRecordVO> getApplyRecordByStudentId(@Param("studentId") Long studentId,
                                                  @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    List<ApplyRecordVO> getApplyRecordByTeacherId(@Param("teacherId") Long teacherId,
                                                  @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    Long getApplyRecordCountByStudentId(Long studentId);

    Long getApplyRecordCountByTeacherId(Long teacherId);

    Long getClassRoomStudentCount(Long classRoomId);

    List<ClassStudentVO> getClassRoomStudentList(@Param("classRoomId") Long classRoomId,
                                                 @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    Long getClassRoomCountByTeacherId(Long teacherId);

    List<ClassRoom> getClassRoomByTeacherId(@Param("teacherId") Long teacherId,
                                            @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    Long getClassRoomCountByStudentId(Long studentId);

    List<ClassRoom> getCLassRoomByStudentId(@Param("studentId") Long studentId,
                                            @Param("start") Integer start,@Param("pageSize") Integer pageSize);

    User getTeacherByClassRoomId(Long classRoomId);

    List<ClassRoom> getClassRoomByTeacherIdAndClassName(@Param("teacherId") Long teacherId, @Param("className") String className,
                                                        @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    List<ClassRoom> getClassRoomStudentNotJoin(@Param("studentId") Long StudentId, @Param("className") String className,
                                               @Param("creator") String creator, @Param("start") Integer pageNo,
                                               @Param("pageSize") Integer pageSize);

    Long getClassRoomStudentNotJoinCount(Long studentId);

    Long getCLassRoomCountByTeacherIdAndClassName(@Param("teacherId") Long teacherId, @Param("className") String className);

    Long getClassRoomStudentNotJoinCountByCondition(@Param("studentId") Long studentId, @Param("className") String className,
                                                    @Param("creator") String creator);
}
