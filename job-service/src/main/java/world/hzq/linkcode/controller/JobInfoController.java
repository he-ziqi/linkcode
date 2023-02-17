package world.hzq.linkcode.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import world.hzq.linkcode.entity.Job;
import world.hzq.linkcode.service.JobInfoService;
import world.hzq.linkcode.util.Result;

import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description 作业信息获取
 * @date 2023/2/14 00:11
 */
@Api(tags = "作业信息获取")
@RestController
@RequestMapping("/info")
public class JobInfoController {

    @Autowired
    private JobInfoService jobInfoService;

    @ApiOperation("获取班级作业信息")
    @GetMapping("/get/job/info/{pageNo}/{pageSize}")
    public Result<Job> getJobInfo(@ApiParam("页码") @PathVariable("pageNo") Integer pageNo,
                                  @ApiParam("分页大小") @PathVariable("pageSize") Integer pageSize,
                                  @ApiParam("班级id") @RequestParam("classRoomId") Long classRoomId){
        return jobInfoService.getJobInfo(classRoomId,pageNo,pageSize);
    }

    @ApiOperation("获取作业类型")
    @GetMapping("/get/job/type")
    public Result<List<String>> getJobType(){
        return jobInfoService.getJobType();
    }

    @ApiOperation("获取作业状态类型")
    @GetMapping("/get/job/status/type")
    public Result<List<String>> getJobStatusType(){
        return jobInfoService.getJobStatusType();
    }
}
