package world.hzq.linkcode.service.impl.strategy.login;

import org.springframework.stereotype.Service;
import world.hzq.linkcode.dto.LoginDTO;
import world.hzq.linkcode.entity.User;
import world.hzq.linkcode.mapper.UserManagementMapper;
import world.hzq.linkcode.service.LoginStrategyService;
import world.hzq.linkcode.type.RoleType;
import world.hzq.linkcode.util.Result;
import world.hzq.linkcode.util.Tools;

import javax.annotation.Resource;

/**
 * @author hzq
 * @version 1.0
 * @description 用户名密码登录策略实现
 * @date 2023/2/7 14:43
 */
@Service
public class NickNameLoginStrategyServiceImpl implements LoginStrategyService {

    @Resource
    private UserManagementMapper userManagementMapper;

    @Override
    public Result<User> login(LoginDTO loginDTO) {
        String nickName = loginDTO.getNickName();
        String password = loginDTO.getPassword();
        String roleType = loginDTO.getRoleType();
        if(Tools.isNull(nickName)){
            return Result.fail("昵称不能为空哦~");
        }
        if(Tools.isNull(password)){
            return Result.fail("密码不能为空哦~");
        }
        User user = userManagementMapper.getUserByNickNameAndPassword(nickName,password,roleType);
        if(user == null){
            return Result.fail("昵称或密码或角色类型不正确");
        }
        user.setRoleType(roleType);
        return Result.success(user);
    }
}
