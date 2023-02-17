package world.hzq.linkcode.strategy.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import world.hzq.linkcode.service.LoginStrategyService;
import world.hzq.linkcode.type.LoginType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hzq
 * @version 1.0
 * @description 登录策略工厂
 * @date 2023/2/7 14:39
 */
@Component
public class LoginStrategyFactory {
    @Autowired
    @Qualifier("nickNameLoginStrategyServiceImpl")
    private LoginStrategyService nickNameLoginStrategyServiceImpl;

    @Autowired
    @Qualifier("phonePasswordLoginStrategyServiceImpl")
    private LoginStrategyService phonePasswordLoginStrategyServiceImpl;

    @Autowired
    @Qualifier("emailVerificationLoginStrategyServiceImpl")
    private LoginStrategyService emailVerificationLoginStrategyServiceImpl;

    //策略服务map
    private static final Map<String,LoginStrategyService> LOGIN_STRATEGY_SERVICE_MAP = new HashMap<>();

    //只能通过注入方式 必须在策略服务对象创建后将其放入到map中
    private void init(){
        LOGIN_STRATEGY_SERVICE_MAP.put(LoginType.NICK_NAME_TYPE.getCode(), nickNameLoginStrategyServiceImpl);
        LOGIN_STRATEGY_SERVICE_MAP.put(LoginType.PHONE_PASSWORD_TYPE.getCode(), phonePasswordLoginStrategyServiceImpl);
        LOGIN_STRATEGY_SERVICE_MAP.put(LoginType.EMAIL_VERIFICATION_TYPE.getCode(), emailVerificationLoginStrategyServiceImpl);
    }

    public LoginStrategyService getLoginStrategy(String loginWay){
        if(LOGIN_STRATEGY_SERVICE_MAP.size() == 0){
            init();
        }
        return LOGIN_STRATEGY_SERVICE_MAP.get(loginWay);
    }

}
