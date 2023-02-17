package world.hzq.linkcode.service.impl.strategy.template;

import org.springframework.stereotype.Service;
import world.hzq.linkcode.entity.MailModel;
import world.hzq.linkcode.service.EmailTemplateStrategyService;

/**
 * @author hzq
 * @version 1.0
 * @description 更新密码邮件模板策略
 * @date 2023/2/9 01:20
 */
@Service
public class UpdatePasswordEmailTemplateStrategy implements EmailTemplateStrategyService {
    @Override
    public MailModel getEmailTemplate(String email, String verification, Long timeout) {
        return new MailModel(email,"修改密码","您本次修改密码的验证码为：" + verification + "验证码将在" + timeout + "秒后过期",true);
    }
}
