package world.hzq.linkcode.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import world.hzq.linkcode.util.Result;
import world.hzq.linkcode.vo.InvokeResult;

import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description 代码执行feign
 * @date 2023/2/16 17:34
 */
@FeignClient(name = "invoke-code")
@Component
public interface InvokeCodeFeign {


    @PostMapping("/code/invoke/{topicId}")
    Result<InvokeResult> executeCode(@RequestParam("code") String code,
                                     @RequestParam("languageType") String languageType,
                                     @PathVariable("topicId") Long topicId);

    @GetMapping("/get/support")
    Result<List<String>> getSupportLanguage();
}
