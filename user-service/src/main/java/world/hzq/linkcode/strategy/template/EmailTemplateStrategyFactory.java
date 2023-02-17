package world.hzq.linkcode.strategy.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import world.hzq.linkcode.service.EmailTemplateStrategyService;
import world.hzq.linkcode.type.EmailType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hzq
 * @version 1.0
 * @description 邮件模板策略工厂
 * @date 2023/2/9 01:10
 */
@Component
public class EmailTemplateStrategyFactory {
    @Autowired
    @Qualifier("loginEmailTemplateStrategy")
    private EmailTemplateStrategyService loginEmailTemplateStrategy;

    @Autowired
    @Qualifier("updatePasswordEmailTemplateStrategy")
    private EmailTemplateStrategyService updatePasswordEmailTemplateStrategy;

    @Autowired
    @Qualifier("findPasswordEmailTemplateStrategy")
    private EmailTemplateStrategyService findPasswordEmailTemplateStrategy;

    //策略服务map
    private static final Map<String, EmailTemplateStrategyService> EMAIL_TEMPLATE_STRATEGY_SERVICE_MAP = new HashMap<>();

    //只能通过注入方式 必须在策略服务对象创建后将其放入到map中
    private void init(){
        EMAIL_TEMPLATE_STRATEGY_SERVICE_MAP.put(EmailType.LOGIN_OPERATION_TYPE.getCode(), loginEmailTemplateStrategy);
        EMAIL_TEMPLATE_STRATEGY_SERVICE_MAP.put(EmailType.UPDATE_PASSWORD_OPERATION_TYPE.getCode(), updatePasswordEmailTemplateStrategy);
        EMAIL_TEMPLATE_STRATEGY_SERVICE_MAP.put(EmailType.FIND_PASSWORD_OPERATION_TYPE.getCode(), findPasswordEmailTemplateStrategy);
    }

    public EmailTemplateStrategyService getEmailTemplateStrategy(String way){
        if(EMAIL_TEMPLATE_STRATEGY_SERVICE_MAP.size() == 0){
            init();
        }
        return EMAIL_TEMPLATE_STRATEGY_SERVICE_MAP.get(way);
    }

}
