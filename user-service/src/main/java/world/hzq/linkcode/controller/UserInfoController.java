package world.hzq.linkcode.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import world.hzq.linkcode.annotation.OnlyInternalCall;
import world.hzq.linkcode.entity.SolutionRecord;
import world.hzq.linkcode.service.UserInfoService;
import world.hzq.linkcode.util.Result;
import world.hzq.linkcode.vo.InfoVO;
import world.hzq.linkcode.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/8 21:04
 */
@RestController
@Api(tags = "用户信息获取接口")
@RequestMapping("/info")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    //能走到这说明网关token校验通过,此处不再进行token校验
    @ApiOperation(value = "获取简单用户信息",notes = "必须要登录后携带token才可以获取")
    @GetMapping("/get/simple")
    public Result<UserVO> getUserInfo(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        return userInfoService.getUserInfo(token);
    }

    @ApiOperation("获取头像")
    @GetMapping("/get/avatar")
    public void getAvatar(HttpServletRequest request, HttpServletResponse response){
        userInfoService.getAvatar(request,response);
    }

    @ApiOperation(value = "获取编译路径",notes = "服务内部接口")
    @GetMapping("/get/compile/path")
    @OnlyInternalCall
    public Result<String> getCompilePath(){
        return userInfoService.getCompilePath();
    }

    @ApiOperation("获取代码执行记录")
    @GetMapping("/get/record/{topicId}/{languageType}/{pageNo}/{pageSize}")
    public Result<InfoVO<SolutionRecord>> getInvokeRecord(@ApiParam("题目id") @PathVariable("topicId") Long topicId,
                                                          @ApiParam("语言类型") @PathVariable("languageType") String languageType,
                                                          @ApiParam("页码") @PathVariable("pageNo") Integer pageNo,
                                                          @ApiParam("分页大小") @PathVariable("pageSize") Integer pageSize){
        return userInfoService.getInvokeRecord(topicId,languageType,pageNo,pageSize);
    }

    @ApiOperation("获取执行记录状态类型")
    @GetMapping("/get/record/status/type")
    public Result<List<String>> getInvokeRecordStatusType(){
        return userInfoService.getInvokeRecordStatusType();
    }
}
