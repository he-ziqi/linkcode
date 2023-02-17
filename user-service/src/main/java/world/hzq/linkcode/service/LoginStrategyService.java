package world.hzq.linkcode.service;

import world.hzq.linkcode.dto.LoginDTO;
import world.hzq.linkcode.entity.User;
import world.hzq.linkcode.util.Result;

/**
 * @author hzq
 * @version 1.0
 * @description 登录策略工厂
 * @date 2023/2/7 14:40
 */
public interface LoginStrategyService {
    /**
     * @description 登录接口
     * @param: param
     * @return: world.hzq.sjm.entity.User
     * @author hzq
     * @date 2023/2/7 14:41
     */
    Result<User> login(LoginDTO loginDTO);
}
