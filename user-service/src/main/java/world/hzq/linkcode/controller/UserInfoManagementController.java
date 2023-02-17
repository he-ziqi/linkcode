package world.hzq.linkcode.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import world.hzq.linkcode.annotation.InterfaceLimit;
import world.hzq.linkcode.dto.FindPasswordDTO;
import world.hzq.linkcode.dto.LoginDTO;
import world.hzq.linkcode.dto.RegistrationDTO;
import world.hzq.linkcode.dto.UpdatePwdDTO;
import world.hzq.linkcode.entity.User;
import world.hzq.linkcode.service.UserManagementService;
import world.hzq.linkcode.type.RoleType;
import world.hzq.linkcode.util.Result;
import world.hzq.linkcode.util.Tools;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author hzq
 * @version 1.0
 * @description 用户注册服务
 * @date 2023/2/6 15:43
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户信息管理接口")
public class UserInfoManagementController {

    @Autowired
    private UserManagementService userManagementService;

    @ApiOperation(value = "用户注册接口",notes = "用户注册接口描述~")
    @PostMapping("/registration")
    public Result<String> registerUser(@ApiParam("用户注册信息实体") @RequestBody RegistrationDTO userInfo){
        return userManagementService.register(userInfo);
    }

    @ApiOperation(value = "用户登录接口",notes = "可选邮箱加验证码或用户名加密码或手机号加密码登录")
    @PostMapping("/sign/in")
    public Result<String> signIn(@ApiParam("登录信息实体") @RequestBody LoginDTO loginDTO){
        String roleType = loginDTO.getRoleType();
        RoleType t = null;
        for (RoleType type : RoleType.values()) {
            if(Tools.equals(type.getCode(),roleType)){
                t = type;
                break;
            }
        }
        if(Tools.isNull(t)){
            return Result.fail("非法角色");
        }
        return userManagementService.signIn(loginDTO);
    }

    @ApiOperation(value = "获取邮箱验证码",notes = "获取邮箱验证码接口限制150秒内访问一次")
    @GetMapping("/get/verification/code/{email}/{operationType}")
    @InterfaceLimit(time = 60,count = 1,timeunit = TimeUnit.SECONDS)
    public Result<String> getVerificationCode(@ApiParam("邮箱地址") @PathVariable("email") String email,
                                              @ApiParam("操作类型") @PathVariable("operationType") String operationType){
        return userManagementService.getVerificationCode(email,operationType);
    }

    @ApiOperation("获取角色类型")
    @GetMapping("/get/role/type")
    public Result<List<String>> getRoleType(){
        return userManagementService.getRoleType();
    }

    @ApiOperation("获取登录方式集合")
    @GetMapping("/get/login/type")
    public Result<List<String>> getLoginType(){
        return userManagementService.getLoginType();
    }

    @ApiOperation("获取验证码操作类型")
    @GetMapping("/get/verification/code/type")
    public Result<List<String>> getVerificationCodeType(){
        return userManagementService.getVerificationCodeType();
    }

    @ApiOperation("修改密码")
    @PostMapping("/update/pwd")
    public Result<String> updatePassword(@ApiParam("更新密码信息传输实体") @RequestBody UpdatePwdDTO updatePwdDTO){
        return userManagementService.updatePassword(updatePwdDTO);
    }

    @ApiOperation("找回密码")
    @PostMapping("/find/account")
    public Result<String> findPassword(@ApiParam("找回密码信息传输实体") @RequestBody FindPasswordDTO findPasswordDTO){
        return userManagementService.findPassword(findPasswordDTO);
    }

    @ApiOperation(value = "更新头像",notes = "头像大小限制20MB")
    @PostMapping(value = "/update/avatar",consumes = "multipart/form-data")
    @InterfaceLimit(time = 1,count = 1,timeunit = TimeUnit.DAYS)
    public Result<String> updateAvatar(@ApiParam("头像文件") @RequestPart("file") MultipartFile file){
        return userManagementService.updateAvatar(file);
    }

    @ApiOperation("退出登录")
    @DeleteMapping("/sign/out")
    public Result<String> signOut(){
        return userManagementService.logOut();
    }

}
