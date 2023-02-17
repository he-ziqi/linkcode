package world.hzq.linkcode.service.impl.strategy.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import world.hzq.linkcode.dto.LoginDTO;
import world.hzq.linkcode.entity.User;
import world.hzq.linkcode.mapper.UserManagementMapper;
import world.hzq.linkcode.service.LoginStrategyService;
import world.hzq.linkcode.type.RoleType;
import world.hzq.linkcode.util.Result;
import world.hzq.linkcode.util.Tools;

/**
 * @author hzq
 * @version 1.0
 * @description 手机号+密码登录策略实现
 * @date 2023/2/7 14:44
 */
@Service
public class PhonePasswordLoginStrategyServiceImpl implements LoginStrategyService {
    @Autowired
    private UserManagementMapper userManagementMapper;

    @Override
    public Result<User> login(LoginDTO loginDTO) {
        String password = loginDTO.getPassword();
        String phoneNumber = loginDTO.getPhoneNumber();
        String roleType = loginDTO.getRoleType();
        if(Tools.isNull(password)){
            return Result.fail("密码不能为空");
        }
        if(Tools.isNull(phoneNumber)){
            return Result.fail("手机号不能为空");
        }
        if(!Tools.isPhoneNumber(phoneNumber)){
            return Result.fail("手机号不正确,再检查下哦~");
        }
        User user = userManagementMapper.getUserByPhoneNumberAndPassword(phoneNumber,password,roleType);
        if(user == null){
            return Result.fail("手机号或密码或角色类型不正确");
        }
        user.setRoleType(roleType);
        return Result.success(user);
    }
}
