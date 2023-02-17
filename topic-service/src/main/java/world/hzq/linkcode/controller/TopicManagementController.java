package world.hzq.linkcode.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import world.hzq.linkcode.annotation.OnlyInternalCall;
import world.hzq.linkcode.dto.AddTopicDTO;
import world.hzq.linkcode.entity.Solution;
import world.hzq.linkcode.entity.TopicInput;
import world.hzq.linkcode.entity.TopicOutput;
import world.hzq.linkcode.service.TopicManagementService;
import world.hzq.linkcode.util.Result;

import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description 题目管理
 * @date 2023/2/2 17:45
 */
@RestController
@RequestMapping("/manage")
@Api(tags = "题目管理接口")
@Slf4j
public class TopicManagementController {

    private static final Logger logger = LoggerFactory.getLogger(TopicManagementController.class);

    @Autowired
    private TopicManagementService topicManagementService;

    @ApiOperation(value = "添加题目",notes = "必须包含题目信息对象和至少一个输入集对象,题解对象可不包含,不包含的对象传空即可")
    @PostMapping("/add/topic/info")
    public Result<String> addTopic(@ApiParam("题目信息传输对象") @RequestBody AddTopicDTO topicDTO){
        Result<String> result = null;
        try {
            result = topicManagementService.addTopic(topicDTO);
        } catch (Exception e) {
            logger.info("添加题目失败 : {}",e.getMessage());
            result = Result.fail("添加题目失败");
        }
        return result;
    }

    @ApiOperation("删除题目")
    @DeleteMapping("/remove/topic/{id}")
    public Result<String> removeTopic(@ApiParam("题目id") @PathVariable("id") Long id){
        return topicManagementService.removeTopic(id);
    }

    @ApiOperation(value = "更新题目信息",notes = "题目信息对象、输入集对象、题解对象至少需要包含一个,如只进行题目信息对象的更新,其它两个对象传空即可")
    @PutMapping("/update/topic/all")
    public Result<String> updateTopic(@ApiParam("题目信息传输对象") @RequestBody AddTopicDTO topicDTO){
        return topicManagementService.updateTopic(topicDTO);
    }

    @ApiOperation(value = "增加输入集",notes = "至少需要传一个输入集对象,且必须放在集合中")
    @PostMapping("/add/topic/input")
    public Result<String> addTopicInput(@ApiParam("输入集对象列表") @RequestBody List<TopicInput> topicInputList){
        return topicManagementService.addTopicInput(topicInputList);
    }

    @ApiOperation(value = "增加题解",notes = "至少需要传一个题解对象,且必须放在集合中,创建时间和id不用填")
    @PostMapping("/add/topic/solution")
    public Result<String> addTopicSolution(@ApiParam("题解对象列表") @RequestBody List<Solution> topicSolutionList){
        return topicManagementService.addTopicSolution(topicSolutionList);
    }

    @ApiOperation(value = "添加输出集",notes = "服务内部调用使用")
    @PostMapping("/add/topic/output")
    @OnlyInternalCall
    public Result<String> addTopicOutput(@ApiParam("输出集对象列表") @RequestBody List<TopicOutput> outputList){
        return topicManagementService.addTopicOutput(outputList);
    }

}
