package world.hzq.linkcode.service;

import org.springframework.web.multipart.MultipartFile;
import world.hzq.linkcode.dto.FindPasswordDTO;
import world.hzq.linkcode.dto.LoginDTO;
import world.hzq.linkcode.dto.RegistrationDTO;
import world.hzq.linkcode.dto.UpdatePwdDTO;
import world.hzq.linkcode.entity.User;
import world.hzq.linkcode.util.Result;

import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description 用户管理服务
 * @date 2023/2/6 19:18
 */
public interface UserManagementService {
    /**
     * @description 用户注册实现
     * @param: userInfo 用户注册信息实体
     * @return: world.hzq.sjm.util.Result<java.lang.String>
     * @author hzq
     * @date 2023/2/6 19:20
     */
    Result<String> register(RegistrationDTO userInfo);

    /**
     * @description 获取登录类型码表
     * @return: world.hzq.sjm.util.Result<java.util.List<java.lang.String>>
     * @author hzq
     * @date 2023/2/9 10:33
     */
    Result<List<String>> getLoginType();

    /**
     * @description 获取邮箱验证码
     * @param: email 邮件地址
     * @param operationType 操作类型
     * @return: world.hzq.sjm.util.Result<java.lang.String>
     * @author hzq
     * @date 2023/2/6 22:45
     */
    Result<String> getVerificationCode(String email,String operationType);

    /**
     * @description 登录方法
     * @param: loginDTO
     * @return: 用户信息展示实体
     * @author hzq
     * @date 2023/2/7 14:58
     */
    Result<String> signIn(LoginDTO loginDTO);

    /**
     * @description 修改密码
     * @param: updatePwdDTO
     * @return: world.hzq.sjm.util.Result<java.lang.String>
     * @author hzq
     * @date 2023/2/9 00:58
     */
    Result<String> updatePassword(UpdatePwdDTO updatePwdDTO);

    /**
     * @description 获取验证码类型
     * @return: world.hzq.sjm.util.Result<java.util.List<java.lang.String>>
     * @author hzq
     * @date 2023/2/9 01:07
     */
    Result<List<String>> getVerificationCodeType();

    /**
     * @description 更新头像
     * @param: file
     * @return: world.hzq.sjm.util.Result<java.lang.String>
     * @author hzq
     * @date 2023/2/9 02:55
     */
    Result<String> updateAvatar(MultipartFile file);

    /**
     * @description 找回密码
     * @param: findPasswordDTO
     * @return: world.hzq.sjm.util.Result<java.lang.String>
     * @author hzq
     * @date 2023/2/9 10:33
     */
    Result<String> findPassword(FindPasswordDTO findPasswordDTO);

    /**
     * @description 退出登录
     * @return: world.hzq.sjm.util.Result<java.lang.String>
     * @author hzq
     * @date 2023/2/9 10:53
     */
    Result<String> logOut();

    /**
     * @description 获取角色信息
     * @return: world.hzq.linkcode.util.Result<java.util.List<java.lang.String>>
     * @author hzq
     * @date 2023/2/12 16:30
     */
    Result<List<String>> getRoleType();

}
