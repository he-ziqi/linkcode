package world.hzq.linkcode.service;

import world.hzq.linkcode.entity.MailModel;
import world.hzq.linkcode.util.Result;

import java.io.File;

public interface MailService {
    //发送不带附件的邮件
    Result<String> sendMail(MailModel mailModel);
    //发送带附件的邮件
    Result<String> sendAttachmentMail(MailModel mailModel,File[] fileList);
}