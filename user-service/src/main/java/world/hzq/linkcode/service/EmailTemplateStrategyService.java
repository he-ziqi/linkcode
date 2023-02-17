package world.hzq.linkcode.service;

import world.hzq.linkcode.entity.MailModel;

/**
 * @author hzq
 * @version 1.0
 * @description 邮件模板策略服务
 * @date 2023/2/9 01:11
 */
public interface EmailTemplateStrategyService {
    /**
     * @description 获取邮件模版
     * @param: email 邮件地址
     * @param: verification 验证码
     * @param timeout 过期时间
     * @return: world.hzq.sjm.entity.MailModel
     * @author hzq
     * @date 2023/2/9 01:15
     */
    MailModel getEmailTemplate(String email,String verification,Long timeout);
}
