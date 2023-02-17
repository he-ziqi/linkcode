package world.hzq.linkcode.service.impl.strategy.template;

import org.springframework.stereotype.Service;
import world.hzq.linkcode.entity.MailModel;
import world.hzq.linkcode.service.EmailTemplateStrategyService;

/**
 * @author hzq
 * @version 1.0
 * @description 登录邮件模版策略
 * @date 2023/2/9 01:17
 */
@Service
public class LoginEmailTemplateStrategy implements EmailTemplateStrategyService {
    @Override
    public MailModel getEmailTemplate(String email, String verification,Long timeout) {
        return new MailModel(email,"登录验证","您本次登录验证码为：" + verification + "验证码将在" + timeout + "秒后过期",true);
    }
}
