package world.hzq.linkcode.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import world.hzq.linkcode.dto.AddChoiceTopicDTO;
import world.hzq.linkcode.dto.AddProgramingTopicDTO;
import world.hzq.linkcode.dto.SubmitJobDTO;
import world.hzq.linkcode.type.JobCommitStatusType;

import java.util.Date;
import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/13 21:01
 */
@Mapper
public interface JobManagementMapper {
    Integer addChoiceTopic(AddChoiceTopicDTO addChoiceTopicDTO);

    Integer bulkAddChoiceTopic(List<AddChoiceTopicDTO> addChoiceTopicDTOList);

    Integer bulkAddProgramTopic(List<AddProgramingTopicDTO> addProgramingTopicDTOList);

    List<Long> getClassRoomStudentIdList(Long classRoomId);

    Integer addTopicCommitRecord(@Param("jobId") Long jobId,
                                 @Param("type") String type,
                                 @Param("studentIdList") List<Long> studentIdList,
                                 @Param("status") String status,
                                 @Param("classRoomId") Long classRoomId);

    Integer updateJobCommitRecord(@Param("submitJobDTO") SubmitJobDTO submitJobDTO,
                                  @Param("studentId") Long studentId,
                                  @Param("status") String status,
                                  @Param("commitTime") Date commitTime,
                                  @Param("ac") Short ac);

    Date getChoiceTopicStopTime(Long jobId);

    Date getProgramTopicStopTime(Long jobId);

    String getChoiceTopicAnswer(Long jobId);

    Long getRealProgramTopicId(Long id);
}
