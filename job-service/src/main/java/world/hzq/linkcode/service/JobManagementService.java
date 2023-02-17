package world.hzq.linkcode.service;

import world.hzq.linkcode.dto.AddChoiceTopicDTO;
import world.hzq.linkcode.dto.AddProgramingTopicDTO;
import world.hzq.linkcode.dto.SubmitJobDTO;
import world.hzq.linkcode.util.Result;
import world.hzq.linkcode.vo.StudentJob;
import world.hzq.linkcode.vo.TopicSubmitResultVO;

import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description 作业管理服务
 * @date 2023/2/13 20:17
 */
public interface JobManagementService {

    /**
     * @description 发布单个选择题
     * @param: addChoiceTopicDTO
     * @return: world.hzq.linkcode.util.Result<java.lang.String>
     * @author hzq
     * @date 2023/2/13 20:35
     */
    Result<String> releaseChoiceJob(AddChoiceTopicDTO addChoiceTopicDTO);

    /**
     * @description 批量发布选择题
     * @param: addChoiceTopicDTOList
     * @return: world.hzq.linkcode.util.Result<java.lang.String>
     * @author hzq
     * @date 2023/2/13 20:35
     */
    Result<String> bulkReleaseChoiceJob(List<AddChoiceTopicDTO> addChoiceTopicDTOList);

    /**
     * @description 批量发布编程题
     * @param: addProgramingTopicDTOList
     * @return: world.hzq.linkcode.util.Result<java.lang.String>
     * @author hzq
     * @date 2023/2/13 23:40
     */
    Result<String> bulkReleaseProgram(List<AddProgramingTopicDTO> addProgramingTopicDTOList);

    /**
     * @description 提交作业
     * @param: submitJobDTO
     * @return: world.hzq.linkcode.util.Result<world.hzq.linkcode.vo.StudentJob>
     * @author hzq
     * @date 2023/2/15 19:06
     */
    Result<TopicSubmitResultVO> submitJob(SubmitJobDTO submitJobDTO);
}
