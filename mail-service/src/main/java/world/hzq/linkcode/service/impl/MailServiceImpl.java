package world.hzq.linkcode.service.impl;

import world.hzq.linkcode.entity.MailModel;
import world.hzq.linkcode.service.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import world.hzq.linkcode.util.Result;
import world.hzq.linkcode.util.Tools;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Date;

@Service
@PropertySource("classpath:application.yml")
public class MailServiceImpl implements MailService {
    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sendUser;

    //发送不带附件的邮件
    @Override
    public Result<String> sendMail(MailModel mailModel) {
        if(mailModel.getTarget() != null && !Tools.isMail(mailModel.getTarget())){
            return Result.badRequest("this is not a mail");
        }
        //对参数进行合法性判断
        if(isIllegal(mailModel)){
            return Result.badRequest("param error");
        }
        //获取各个参数
        String target = mailModel.getTarget();
        String content = mailModel.getContent();
        String title = mailModel.getTitle();
        Boolean html = mailModel.getHtml();

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper send = new MimeMessageHelper(mimeMessage);
        try {
            //设置发送人
            send.setFrom(sendUser);
            send.setTo(target);
            send.setSubject(title);
            send.setSentDate(new Date()); //发送时间
            send.setText(content,html);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("send fail");
        }
        return Result.success("success");
    }

    //发送带附件的邮件
    @Override
    public Result<String> sendAttachmentMail(MailModel mailModel,File[] fileList) {
        if(mailModel.getTarget() != null && !Tools.isMail(mailModel.getTarget())){
            return Result.badRequest("this is not a mail");
        }
        //对参数进行合法性判断
        if(isIllegal(mailModel,fileList)){
            return Result.badRequest("param error");
        }
        //获取各个参数
        String target = mailModel.getTarget();
        String content = mailModel.getContent();
        String title = mailModel.getTitle();
        Boolean html = mailModel.getHtml();

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper send = new MimeMessageHelper(mimeMessage,true);
            send.setSubject(title);
            send.setFrom(sendUser);
            send.setTo(target);
            send.setSentDate(new Date());
            send.setText(content,html);
            for (File file : fileList) {
                if(file != null){
                    send.addAttachment(file.getName(),file);
                }
            }
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("send fail");
        }
        return Result.success("success");
    }

    /**
     * 对参数合法性进行判断
     * @param mailModel 参数
     */
    private boolean isIllegal(MailModel mailModel){
        if(mailModel == null || mailModel.getTitle() == null || "".equals(mailModel.getTitle())
        || mailModel.getContent() == null || "".equals(mailModel.getContent())
        || mailModel.getHtml() == null){
            return true;
        }
        return false;
    }

    private boolean isIllegal(MailModel mailModel,File[] files){
        return isIllegal(mailModel) || files == null;
    }
}