package world.hzq.linkcode.feign;

import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import world.hzq.linkcode.entity.Solution;
import world.hzq.linkcode.entity.Topic;
import world.hzq.linkcode.entity.TopicInput;
import world.hzq.linkcode.entity.TopicOutput;
import world.hzq.linkcode.util.Result;
import world.hzq.linkcode.vo.InfoVO;

import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/5 21:22
 */
@FeignClient(name = "topic-service")
public interface TopicOperationFeign {

    //获取题目信息
    @GetMapping("/info/get/topic/info/{id}")
    Result<Topic> getTopicInfo(@PathVariable("id") Long id);

    //获取输入集
    @GetMapping("/info/get/topic/input/{id}")
    Result<List<TopicInput>> getTopicInput(@PathVariable("id") Long id);

    //获取输出集列表
    @GetMapping("/info/get/topic/output")
    Result<List<TopicOutput>> getTopicOutput(@RequestParam("topicInputIdList") List<Long> topicInputIdList);

    //获取题解信息
    @GetMapping("/info/get/topic/solution/{topicId}/{languageType}/{pageNo}/{pageSize}")
    Result<InfoVO<Solution>> getSolutionList(@PathVariable("topicId") Long topicId,
                                             @PathVariable("languageType") String languageType,
                                             @PathVariable("pageNo") Integer pageNo,
                                             @PathVariable("pageSize") Integer pageSize);

    //添加输出集
    @PostMapping("/manage/add/topic/output")
    Result<String> addTopicOutput(@ApiParam("输出集对象列表") @RequestBody List<TopicOutput> outputList);
}
