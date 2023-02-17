package world.hzq.linkcode.service.impl.strategy.login;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import world.hzq.linkcode.dto.LoginDTO;
import world.hzq.linkcode.entity.User;
import world.hzq.linkcode.mapper.UserManagementMapper;
import world.hzq.linkcode.service.LoginStrategyService;
import world.hzq.linkcode.type.EmailType;
import world.hzq.linkcode.util.Result;
import world.hzq.linkcode.util.Tools;

import javax.annotation.Resource;


/**
 * @author hzq
 * @version 1.0
 * @description 邮箱+验证码登录策略实现
 * @date 2023/2/7 14:45
 */
@Service
public class EmailVerificationLoginStrategyServiceImpl implements LoginStrategyService {

    @Resource
    private UserManagementMapper userManagementMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result<User> login(LoginDTO loginDTO) {
        String verificationCode = loginDTO.getVerificationCode();
        String email = loginDTO.getEmail();
        String roleType = loginDTO.getRoleType();
        if(!Tools.isMail(email)){
            return Result.fail("邮件不正确");
        }
        if(Tools.isNull(verificationCode)){
            return Result.fail("验证码不能为空");
        }
        User user = userManagementMapper.getUserByEmailAndRole(email,roleType);
        if(user == null){
            return Result.fail("该邮箱未注册或角色类型错误");
        }
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        //从redis获取值
        String code = ops.get(email + ":" + EmailType.LOGIN_OPERATION_TYPE.getCode());
        if(Tools.isNull(code)){
            return Result.fail("验证码已过期");
        }
        //统一转为大写,进行验证(实现大小写均可验证)
        if(!Tools.sameVerificationCode(code,verificationCode)){
            return Result.fail("验证码不正确");
        }
        user.setRoleType(roleType);
        return Result.success(user);
    }
}
