package world.hzq.linkcode.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import world.hzq.linkcode.entity.MailModel;
import world.hzq.linkcode.util.Result;


/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/7 13:16
 */
@FeignClient(name = "mail-service",path = "/mail")
public interface MailFeign {

    @PostMapping(value = "/send/simple",consumes = "application/json")
    Result<String> sendMail(@RequestBody MailModel mail);

    @PostMapping(value = "/send/attachment",consumes = "multipart/form-data")
    Result<String> sendAttachmentMail(MailModel mail,
                                      @RequestParam("file") MultipartFile[] file);
}
