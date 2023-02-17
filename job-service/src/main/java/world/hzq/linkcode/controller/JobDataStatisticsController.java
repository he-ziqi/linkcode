package world.hzq.linkcode.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import world.hzq.linkcode.service.JobDataStatisticsService;
import world.hzq.linkcode.util.Result;
import world.hzq.linkcode.vo.JobDataStatisticsVO;

/**
 * @author hzq
 * @version 1.0
 * @description 作业数据统计
 * @date 2023/2/16 15:57
 */
@RestController
@RequestMapping("/statistics")
@Api(tags = "作业数据统计")
public class JobDataStatisticsController {

    @Autowired
    private JobDataStatisticsService jobDataStatisticsService;

    @ApiOperation("选择题数据统计")
    @GetMapping("/topic/choice/{classRoomId}/{topicId}")
    public Result<JobDataStatisticsVO> choiceTopicStatistics(@ApiParam("班级id") @PathVariable("classRoomId") Long classRoomId,
                                                             @ApiParam("选择题题目id") @PathVariable("topicId") Long topicId){
        return jobDataStatisticsService.choiceTopicInfoStatistics(classRoomId,topicId);
    }

    @ApiOperation("编程题数据统计")
    @GetMapping("/topic/program/{classRoomId}/{topicId}")
    public Result<JobDataStatisticsVO> programTopicInfoStatistics(@ApiParam("班级id") @PathVariable("classRoomId") Long classRoomId,
                                                                  @ApiParam("编程题题目id") @PathVariable("topicId") Long topicId){
        return jobDataStatisticsService.programTopicInfoStatistics(classRoomId,topicId);
    }

}
