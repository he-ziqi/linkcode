package world.hzq.linkcode.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import world.hzq.linkcode.util.Result;


/**
 * @author hzq
 * @version 1.0
 * @description 文件feign接口
 * @date 2023/2/9 02:24
 */
@FeignClient(name = "file-service")
@Component
public interface FileCenterFeign {

    //单文件上传
    @PostMapping(value = "/upload/file/path",consumes = "multipart/form-data")
    Result<String> uploadInner(@RequestPart("file") MultipartFile file,
                               @RequestParam("permanent") Boolean permanent,
                               @RequestParam("path") String path);

    //删除文件
    @DeleteMapping("/info/delete")
    Result<String> deleteFile(@RequestParam("path") String path);
}
