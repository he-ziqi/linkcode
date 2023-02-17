package world.hzq.linkcode.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import world.hzq.linkcode.annotation.OnlyInternalCall;
import world.hzq.linkcode.entity.MailModel;
import world.hzq.linkcode.service.MailService;
import world.hzq.linkcode.service.UploadService;
import world.hzq.linkcode.util.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.multipart.MultipartFile;
import world.hzq.linkcode.util.Result;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

@RestController
@Slf4j
@RequestMapping("/mail")
@Api(tags = "邮件发送接口")
public class MailController {

    @Autowired
    private MailService mailService;

    @Autowired
    private UploadService uploadService;

    /**
     * 每过12个小时清理一次上传目录
     */
    @Scheduled(fixedDelay = 1000 * 60 * 60 * 12)
    public void clear(){
        uploadService.deleteFile();
    }

    @ApiOperation(value = "文本邮件发送",notes = "文本可以是html代码")
    @PostMapping(value = "/send/simple",consumes = "application/json")
    @OnlyInternalCall
    public Result<String> sendMail(@ApiParam("邮件信息实体") @RequestBody MailModel mail,
                                   HttpServletRequest request){
        log.info("(不带附件的上传)请求ip：" + IPUtil.getRealIp(request) + " ：" + mail.toString());
        return mailService.sendMail(mail);
    }

    @ApiOperation(value = "带附件邮件发送",notes = "除文本内容外,可以携带一个或多个附件")
    @PostMapping(value = "/send/attachment",consumes = "multipart/form-data")
    @OnlyInternalCall
    public Result<String> sendAttachmentMail(@ApiParam("邮件信息实体") MailModel mail,
                                             @ApiParam("附件集合") @RequestParam("file") MultipartFile[] file,
                                             HttpServletRequest request){
        String[] msg = uploadService.uploads(file);
        log.info("(带附件的上传)请求ip：" + IPUtil.getRealIp(request) + " ：" + mail.toString());
        File[] files = new File[msg.length];
        int index = 0;
        for (String path : msg) {
            files[index++] = new File(path);
        }
        return mailService.sendAttachmentMail(mail,files);
    }
}