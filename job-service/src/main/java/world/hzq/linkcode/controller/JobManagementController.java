package world.hzq.linkcode.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import world.hzq.linkcode.dto.AddChoiceTopicDTO;
import world.hzq.linkcode.dto.AddProgramingTopicDTO;
import world.hzq.linkcode.dto.SubmitJobDTO;
import world.hzq.linkcode.service.JobManagementService;
import world.hzq.linkcode.util.Result;
import world.hzq.linkcode.vo.StudentJob;
import world.hzq.linkcode.vo.TopicSubmitResultVO;

import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description 作业管理
 * @date 2023/2/13 20:09
 */
@RestController
@RequestMapping("/manage")
@Api(tags = "作业信息管理")
public class JobManagementController {

    @Autowired
    private JobManagementService jobManagementService;

    @ApiOperation("发布选择题作业")
    @PostMapping("/release/choice")
    public Result<String> releaseChoiceJob(@ApiParam("选择题信息传输实体") @RequestBody AddChoiceTopicDTO addChoiceTopicDTO){
        return jobManagementService.releaseChoiceJob(addChoiceTopicDTO);
    }

    @ApiOperation("批量发布选择题")
    @PostMapping("/release/choice/bulk")
    public Result<String> bulkReleaseChoiceJob(@ApiParam("选择题信息传输实体列表") @RequestBody List<AddChoiceTopicDTO> addChoiceTopicDTOList){
        return jobManagementService.bulkReleaseChoiceJob(addChoiceTopicDTOList);
    }

    @ApiOperation("批量发布编程题作业")
    @PostMapping("/release/program/bulk")
    public Result<String> bulkReleaseProgramingJob(@ApiParam("编程题作业信息传输实体") @RequestBody List<AddProgramingTopicDTO> addProgramingTopicDTOList){
        return jobManagementService.bulkReleaseProgram(addProgramingTopicDTOList);
    }

    @ApiOperation("提交作业")
    @PostMapping("/submit/job")
    public Result<TopicSubmitResultVO> submitJob(@ApiParam("提交作业信息传输实体") @RequestBody SubmitJobDTO submitJobDTO){
        return jobManagementService.submitJob(submitJobDTO);
    }
}
