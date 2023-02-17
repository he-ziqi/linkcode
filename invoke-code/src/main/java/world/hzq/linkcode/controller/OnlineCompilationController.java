package world.hzq.linkcode.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import world.hzq.linkcode.service.CodeInvokeService;
import world.hzq.linkcode.util.Result;
import world.hzq.linkcode.vo.InvokeResult;

import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/2 17:20
 */
@RestController
@RequestMapping("/code")
@Api(tags = "代码执行接口")
@Slf4j
public class OnlineCompilationController {

    @Autowired
    private CodeInvokeService codeInvokeService;

    @ApiOperation("执行指定代码")
    @PostMapping("/invoke/{topicId}")
    public Result<InvokeResult> executeCode(@ApiParam("代码字符串") @RequestParam("code") String code,
                                            @ApiParam("编程语言类型") @RequestParam("languageType") String languageType,
                                            @ApiParam("题目id") @PathVariable("topicId") Long topicId){
        log.info(languageType + "\n" + code);
        Result<InvokeResult> result = codeInvokeService.invokeCode(code, languageType, topicId);
        Result<String> saveResult = codeInvokeService.saveRecord(code,languageType,topicId,result);
        log.info(saveResult.getMsg());
        return result;
    }

    @ApiOperation("获取支持的编程语言集合")
    @GetMapping("/get/support")
    public Result<List<String>> getSupportLanguage(){
        return codeInvokeService.getSupportLanguage();
    }
}
