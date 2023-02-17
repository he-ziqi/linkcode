package world.hzq.linkcode.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import world.hzq.linkcode.dto.FindPasswordDTO;
import world.hzq.linkcode.dto.LoginDTO;
import world.hzq.linkcode.dto.RegistrationDTO;
import world.hzq.linkcode.dto.UpdatePwdDTO;
import world.hzq.linkcode.entity.MailModel;
import world.hzq.linkcode.entity.User;
import world.hzq.linkcode.feign.FileCenterFeign;
import world.hzq.linkcode.feign.MailFeign;
import world.hzq.linkcode.mapper.UserManagementMapper;
import world.hzq.linkcode.service.EmailTemplateStrategyService;
import world.hzq.linkcode.service.LoginStrategyService;
import world.hzq.linkcode.service.UserManagementService;
import world.hzq.linkcode.strategy.login.LoginStrategyFactory;
import world.hzq.linkcode.strategy.template.EmailTemplateStrategyFactory;
import world.hzq.linkcode.type.EmailType;
import world.hzq.linkcode.type.LoginType;
import world.hzq.linkcode.type.RoleType;
import world.hzq.linkcode.util.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author hzq
 * @version 1.0
 * @description 用户管理服务实现
 * @date 2023/2/6 19:18
 */
@Service
@Slf4j
public class UserManagementServiceImpl implements UserManagementService {

    @Resource
    private UserManagementMapper userManagementMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MailFeign mailFeign;

    //登录策略工厂
    @Autowired
    private LoginStrategyFactory loginStrategyFactory;

    //邮件模版策略工厂
    @Autowired
    private EmailTemplateStrategyFactory emailTemplateStrategyFactory;

    @Autowired
    private WebUtil webUtil;

    @Autowired
    private FileCenterFeign fileCenterFeign;

    @Value("${verification-code.length}")
    private Integer verificationCodeLength;

    @Value("${verification-code.timeout}")
    private Long timeout;

    @Value("${md5.salt}")
    private String salt;

    @Value("${login.timeout}")
    private Long loginTimeout;

    @Value("${user.avatar.path}")
    private String avatarPath;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> register(RegistrationDTO userInfo) {
        if(Tools.isNull(userInfo)){
            return Result.fail("信息为空,注册失败");
        }
        if(!Tools.isMail(userInfo.getEmail())){
            return Result.fail("邮箱不正确");
        }
        if(!Tools.isPhoneNumber(userInfo.getPhoneNumber())){
            return Result.fail("电话号码不正确");
        }
        if(!Tools.pwdChecked(userInfo.getPassword())){
            return Result.fail("密码应在6-15位之间");
        }
        User u = null;
        //查找昵称是否存在
        u = userManagementMapper.getUserByNickName(userInfo.getNickName());
        if(u != null){
            return Result.fail("该昵称已存在");
        }
        u = userManagementMapper.getUserByPhoneNumber(userInfo.getPhoneNumber());
        if(u != null){
            return Result.fail("该手机号已被注册");
        }
        RoleType role = null;
        String roleType = userInfo.getRoleType();
        for (RoleType type : RoleType.values()) {
            if(Tools.equals(roleType,type.getCode())){
                role = type;
                break;
            }
        }
        if(role == null){
            return Result.fail("非法角色");
        }
        //查找邮箱是否已被注册
        u = userManagementMapper.getUserByEmail(userInfo.getEmail());
        if(u != null){
            return Result.fail("该邮箱已被注册");
        }
        //用户密码替换
        userInfo.setPassword(MD5Util.getMd5(userInfo.getPassword(),salt));
        //进行注册
        Integer cnt = userManagementMapper.addUser(userInfo);
        //添加角色信息
        Integer inserted = userManagementMapper.addRole(userInfo.getId(),role.getCode(),role.getAuthorityLevel());
        return cnt != 0 && inserted != 0 ? Result.success("注册成功") : Result.fail("注册失败,系统异常");
    }

    @Override
    public Result<List<String>> getLoginType() {
        List<String> types = new ArrayList<>();
        for (LoginType type : LoginType.values()) {
            types.add(type.getCode());
        }
        return Result.success(types);
    }

    //获取邮箱验证码
    @Override
    public Result<String> getVerificationCode(String email,String operationType) {
        if(!Tools.isMail(email)){
            return Result.fail("邮箱不正确");
        }
        User user = userManagementMapper.getUserByEmail(email);
        if(user == null){
            return Result.fail("该邮箱未注册");
        }
        //非法邮件验证码获取类型
        if (!getVerificationCodeType().getData().contains(operationType)) {
            return Result.fail("不允许的邮件操作类型");
        }
        //生成随机验证码
        String verification = VerificationCodeGenerator.generateVerificationCode(verificationCodeLength);
        //获取邮件模版
        EmailTemplateStrategyService emailTemplateStrategy = emailTemplateStrategyFactory.getEmailTemplateStrategy(operationType);
        MailModel mailModel = emailTemplateStrategy.getEmailTemplate(email, verification, timeout);
        //发送邮件
        Result<String> result = mailFeign.sendMail(mailModel);
        if(!Tools.responseOk(result.getCode())){
            return Result.fail("获取验证码失败,请稍后重试");
        }
        //将验证码放到redis中
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set(email + ":" + operationType,verification,timeout, TimeUnit.SECONDS);
        return Result.success("获取验证码成功,请注意查收");
    }

    @Override
    public Result<String> signIn(LoginDTO loginDTO) {
        //已登录无须再次登录
        String tokenStr = webUtil.getToken();
        if(Tools.isNotNull(tokenStr)){
            boolean existsLogin = false;
            String jsonUser = stringRedisTemplate.opsForValue().get(tokenStr);
            if(Tools.isNotNull(jsonUser)){
                User currentUser = JSONObject.parseObject(jsonUser, User.class);
                if(Tools.equals(LoginType.NICK_NAME_TYPE.getCode(),loginDTO.getWay())){
                    if(Tools.equals(currentUser.getNickName(),loginDTO.getNickName())){
                        existsLogin = true;
                    }
                }
                if(Tools.equals(LoginType.PHONE_PASSWORD_TYPE.getCode(),loginDTO.getWay())){
                    if(Tools.equals(currentUser.getPhoneNumber(),loginDTO.getPhoneNumber())){
                        existsLogin = true;
                    }
                }
                if(Tools.equals(LoginType.EMAIL_VERIFICATION_TYPE.getCode(),loginDTO.getWay())){
                    if(Tools.equals(currentUser.getEmail(),loginDTO.getEmail())){
                        existsLogin = true;
                    }
                }
                if(existsLogin){
                    return Result.success("您已登录,无须再次登录",tokenStr);
                }
            }
        }
        //获取登录策略
        LoginStrategyService loginStrategy = loginStrategyFactory.getLoginStrategy(loginDTO.getWay());
        if(Tools.isNull(loginStrategy)){
            return Result.fail("非法登录方式");
        }
        //加盐加密
        loginDTO.setPassword(MD5Util.getMd5(loginDTO.getPassword(),salt));
        Result<User> result = loginStrategy.login(loginDTO);
        if(!Tools.responseOk(result.getCode())){ //登录失败
            return Result.fail(result.getMsg());
        }
        User user = result.getData();
        //放入token payload头信息
        Map<String,String> payload = new HashMap<>();
        payload.put("nickName",user.getNickName());
        payload.put("roleType",user.getRoleType());
        payload.put("info","别想窃取信息了,只有一个昵称和角色");
        //获取token,存入redis
        String token = JwtUtil.createToken(payload);
        //将token存入redis并设置超时(token两倍的超时时间)
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        //存入redis的密码脱敏
        user.setPassword(null);
        ops.set(token, JSON.toJSONString(user),loginTimeout,TimeUnit.SECONDS);
        ops.set(token + ":threshold","0",2 * loginTimeout,TimeUnit.SECONDS); //token刷新次数
        return Result.success("登录成功",token);
    }

    @Override
    public Result<String> updatePassword(UpdatePwdDTO updatePwdDTO) {
        String oldPwd = updatePwdDTO.getOldPwd();
        String newPwd = updatePwdDTO.getNewPwd();
        //检查新密码
        if(!Tools.pwdChecked(newPwd)){
            return Result.fail("密码应在6-15位之间");
        }
        String verificationCode = updatePwdDTO.getVerificationCode();
        User user = webUtil.currentUser();
        if(user == null){
            return Result.fail("修改失败,登录信息已过期,请重新登录");
        }
        String email = user.getEmail();
        String realVerificationCode = stringRedisTemplate.opsForValue().get(email + ":" + EmailType.UPDATE_PASSWORD_OPERATION_TYPE.getCode());
        if(!Tools.sameVerificationCode(verificationCode,realVerificationCode)){
            Result.fail("验证码不正确");
        }
        User userInfo = userManagementMapper.getUserById(user.getId());
        String password = userInfo.getPassword();
        //比对旧密码是否正确
        if(!Tools.equals(password,MD5Util.getMd5(oldPwd,salt))){
            return Result.fail("旧密码不正确");
        }
        //存储新密码到数据库
        Integer updated = userManagementMapper.updatePasswordById(userInfo.getId(),MD5Util.getMd5(newPwd,salt));
        if (updated == 1) {
            //清除redis的token,使登录失效
            Boolean deleted = stringRedisTemplate.delete(webUtil.getToken());
            log.info("用户：" + user + "更新密码成功,删除token：" + deleted);
            return Result.success("修改成功");
        }
        return Result.fail("修改失败,系统异常,请稍后重试");
    }

    @Override
    public Result<List<String>> getVerificationCodeType() {
        List<String> types = new ArrayList<>();
        for (EmailType type : EmailType.values()) {
            types.add(type.getCode());
        }
        return Result.success(types);
    }

    @Override
    public Result<String> updateAvatar(MultipartFile file) {
        Result<String> result = fileCenterFeign.uploadInner(file, true, avatarPath);
        if (!Tools.responseOk(result.getCode())) {
            return Result.fail("头像上传失败,系统异常,请稍后重试");
        }
        User user = webUtil.currentUser();
        //删除之前的头像(不是默认头像情况下)
        String avatarAddr = user.getAvatarAddr();
        if(!Tools.equals("file-center/default.jpg",avatarAddr)){
            //删除结果 feign 调用404 new File不存在===================
            /*Result<String> deleteResult = fileCenterFeign.deleteFile(avatarAddr);
            if(!Tools.responseOk(deleteResult.getCode())){
                return Result.fail("更新失败,现头像为非法存储");
            }*/
        }
        //将头像路径存入数据库
        Integer updated = userManagementMapper.updateAvatarPathById(user.getId(),result.getData());
        if(updated != 1){
            return Result.fail("头像更新失败,系统异常,请稍后重试");
        }
        String token = webUtil.getToken();
        user.setAvatarAddr(result.getData());
        //更新redis中的头像信息
        //重新赋值Token 续签
        stringRedisTemplate.opsForValue().set(token, JSONObject.toJSONString(user),loginTimeout,TimeUnit.SECONDS);
        log.info("头像更新成功,redis中头像路径已刷新");
        return Result.success("头像更新成功");
    }

    @Override
    public Result<String> findPassword(FindPasswordDTO findPasswordDTO) {
        if(Tools.isNull(findPasswordDTO)){
            return Result.fail("信息不能为空");
        }
        if(Tools.isNull(findPasswordDTO.getVerificationCode())){
            return Result.fail("验证码不能为空");
        }
        if(!Tools.isMail(findPasswordDTO.getMail())){
            return Result.fail("邮件地址不正确");
        }
        if(!Tools.isPhoneNumber(findPasswordDTO.getPhoneNumber())){
            return Result.fail("手机号不正确");
        }
        if(!Tools.pwdChecked(findPasswordDTO.getNewPassword())){
            return Result.fail("密码应在6-15位之间");
        }
        String code = stringRedisTemplate.opsForValue().get(findPasswordDTO.getMail() + ":" + EmailType.FIND_PASSWORD_OPERATION_TYPE.getCode());
        if(Tools.isNull(code)){
            return Result.fail("验证码已过期或未获取");
        }
        if(!Tools.sameVerificationCode(findPasswordDTO.getVerificationCode(),code)){
            return Result.fail("验证码不正确");
        }
        User user = userManagementMapper.getUserByEmail(findPasswordDTO.getMail());
        if(user == null){
            return Result.fail("该账号未注册");
        }
        Integer updated = userManagementMapper.updatePasswordByPhoneNumberAndEmail(findPasswordDTO.getPhoneNumber(),
                findPasswordDTO.getMail(),
                MD5Util.getMd5(findPasswordDTO.getNewPassword(),salt));
        return updated == 1 ? Result.success("密码重置成功") : Result.fail("密码重置失败,系统异常,请稍后重试");
    }

    @Override
    public Result<String> logOut() {
        User user = webUtil.currentUser();
        if(user == null){
            //用户信息已过期或未登录
            return Result.success("退出登录成功");
        }
        String token = webUtil.getToken();
        //设置用户token过期
        stringRedisTemplate.opsForValue().set(token,"",1,TimeUnit.SECONDS);
        //统计续签次数过期
        stringRedisTemplate.opsForValue().set(token + ":threshold","-1",1,TimeUnit.SECONDS);
        return Result.success("退出登录成功");
    }

    @Override
    public Result<List<String>> getRoleType() {
        List<String> types = new ArrayList<>(RoleType.values().length);
        for (RoleType type : RoleType.values()) {
            types.add(type.getCode());
        }
        return Result.success(types);
    }

}
