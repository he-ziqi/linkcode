package world.hzq.linkcode.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import world.hzq.linkcode.annotation.OnlyInternalCall;
import world.hzq.linkcode.entity.Solution;
import world.hzq.linkcode.entity.Topic;
import world.hzq.linkcode.entity.TopicInput;
import world.hzq.linkcode.entity.TopicOutput;
import world.hzq.linkcode.service.TopicInfoService;
import world.hzq.linkcode.util.Result;
import world.hzq.linkcode.vo.InfoVO;

import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/2 18:07
 */
@RestController
@RequestMapping("/info")
@Api(tags = "题目信息接口")
public class TopicInfoController {
    //题目信息服务
    @Autowired
    private TopicInfoService topicInfoService;

    @ApiOperation("获取单个题目信息")
    @GetMapping("/get/topic/info/{id}")
    public Result<Topic> getTopicInfo(@ApiParam("题目id") @PathVariable("id") Long id){
        return topicInfoService.getTopicInfo(id);
    }

    @ApiOperation(value = "获取输入集列表",notes = "服务内部调用使用")
    @GetMapping("/get/topic/input/{id}")
    @OnlyInternalCall
    public Result<List<TopicInput>> getTopicInput(@ApiParam("题目id") @PathVariable("id") Long id){
        return topicInfoService.getTopicInputList(id);
    }

    @ApiOperation(value = "获取输出集列表",notes = "服务内部调用使用")
    @GetMapping("/get/topic/output")
    @OnlyInternalCall
    public Result<List<TopicOutput>> getTopicOutput(@ApiParam("题目输入集id集合") @RequestParam("topicInputIdList") List<Long> topicInputIdList){
        return topicInfoService.getTopicOutputList(topicInputIdList);
    }

    @ApiOperation("分页获取题目信息(无条件)")
    @GetMapping("/get/topic/info/{pageNo}/{pageSize}")
    public Result<InfoVO<Topic>> getTopic(@ApiParam("当前页码") @PathVariable("pageNo") Integer pageNo,
                                   @ApiParam("页大小") @PathVariable("pageSize") Integer pageSize){
        return topicInfoService.getTopicInfo(pageNo,pageSize);
    }

    @ApiOperation("获取题目状态码表")
    @GetMapping("/get/topic/status")
    public Result<List<String>> getTopicStatusCodeTable(){
        return topicInfoService.getTopicStatusCodeTable();
    }

    @ApiOperation(value = "分页获取题解信息")
    @GetMapping("/get/topic/solution/{topicId}/{languageType}/{pageNo}/{pageSize}")
    public Result<InfoVO<Solution>> getSolutionList(@ApiParam("题目id") @PathVariable("topicId") Long topicId,
                                                  @ApiParam("语言类型") @PathVariable("languageType") String languageType,
                                                  @ApiParam("页码") @PathVariable("pageNo") Integer pageNo,
                                                  @ApiParam("分页大小") @PathVariable("pageSize") Integer pageSize){
        return topicInfoService.getSolutionList(topicId,languageType,pageNo,pageSize);
    }

    @ApiOperation(value = "分页获取题目信息(有条件)",notes = "通过题目名称或者题目信息获取题目对象,至少包含其中一个参数")
    @GetMapping("/get/topic/info/condition/{pageNo}/{pageSize}")
    public Result<InfoVO<Topic>> getTopicByNameOrComment(@ApiParam("题目名称(模糊匹配)") @RequestParam(value = "topicName",required = false) String topicName,
                                                       @ApiParam("题目内容(模糊匹配)") @RequestParam(value = "topicComment",required = false) String topicComment,
                                                       @ApiParam(value = "当前页码",required = true) @PathVariable("pageNo") Integer pageNo,
                                                       @ApiParam(value = "分页大小",required = true) @PathVariable("pageSize") Integer pageSize,
                                                       @ApiParam(value = "难度升序,0表示升序,1表示降序") @RequestParam(value = "difficulty",required = false) Integer difficulty){
        return topicInfoService.getTopicByNameOrComment(topicName,topicComment,pageNo,pageSize,difficulty);
    }
}
