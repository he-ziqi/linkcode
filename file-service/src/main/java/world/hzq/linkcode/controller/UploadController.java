package world.hzq.linkcode.controller;

import world.hzq.linkcode.annotation.OnlyInternalCall;
import world.hzq.linkcode.service.DownLoadService;
import world.hzq.linkcode.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import world.hzq.linkcode.util.IPUtil;
import world.hzq.linkcode.util.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("/upload")
@Api(tags = "文件上传接口")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @Autowired
    private DownLoadService downLoadService;

    //临时文件会在15天后删除
    @Scheduled(fixedDelay = 1000 * 60 * 60 * 24 * 15)
    public void clear(){
        uploadService.deleteFile();
    }

    /**
     * 单文件上传
     * @param file 单个文件
     * @param permanent 文件是否永久存放
     */
    @ApiOperation(value = "单文件上传接口",notes = "文件大小限制20MB")
    @PostMapping(value = "/file",consumes = "multipart/form-data")
    public Result<String> upload(@ApiParam("文件") @RequestPart("file") MultipartFile file,
                                 @ApiParam("永久保存") Boolean permanent,
                                 HttpServletRequest request){
        return singleFileUpload(file,permanent,null,request);
    }

    @ApiOperation(value = "单文件上传接口(带路径)",notes = "文件大小限制20MB(仅内部调用)")
    @PostMapping(value = "/file/path",consumes = "multipart/form-data")
    @OnlyInternalCall  //仅内部调用
    public Result<String> uploadInner(@ApiParam("文件") @RequestPart("file") MultipartFile file,
                                 @ApiParam("永久保存") Boolean permanent,
                                 @ApiParam("文件路径") String path,
                                 HttpServletRequest request){
        return singleFileUpload(file,permanent,path,request);
    }

    /**
     * 多文件上传
     * @param file 文件数组(支持多个类型,单个文件大小不能超出30MB)
     * @param permanent 文件是否永久存放
     */
    @ApiOperation(value = "多文件上传",notes = "单个文件大小限制20MB,总大小限制200MB")
    @PostMapping(value = "/files",consumes = "multipart/form-data")
    public Result<String[]> uploadFile(@ApiParam("文件数组") @RequestParam("file") @RequestPart MultipartFile[] file,
                                       @ApiParam("永久保存") @RequestParam("permanent") Boolean permanent,
                                       HttpServletRequest request){
        return multipleFileUpload(file,permanent,null,request);
    }

    @ApiOperation(value = "多文件上传(带路径)",notes = "单个文件大小限制20MB,总大小限制200MB(仅内部调用)")
    @PostMapping(value = "/files/path",consumes = "multipart/form-data")
    @OnlyInternalCall
    public Result<String[]> uploadFileInner(@ApiParam("文件数组") @RequestParam("file") @RequestPart MultipartFile[] file,
                                       @ApiParam("永久保存") @RequestParam("permanent") Boolean permanent,
                                       @ApiParam("文件路径") @RequestParam("path") String path,
                                       HttpServletRequest request){
        return multipleFileUpload(file,permanent,path,request);
    }

    private Result<String> singleFileUpload(MultipartFile file,Boolean permanent,String path,HttpServletRequest request){
        String msg = null;
        if(file != null){
            msg = uploadService.upload(file,permanent,path);
        }else {
            return Result.fail("未选择文件");
        }
        if("fail".equals(msg)){
            log.info("单文件上传,请求ip：" + IPUtil.getRealIp(request) + "上传失败!!!");
            return Result.fail("fail");
        }
        log.info("单文件上传,请求ip：" + IPUtil.getRealIp(request) + "上传后的路径：" + msg);
        return path == null ? Result.success("success") : Result.success("success",msg);
    }

    private Result<String[]> multipleFileUpload(MultipartFile[] files,Boolean permanent,String path,HttpServletRequest request){
        String[] res = null;
        if(files != null && files.length > 0){
            res = uploadService.uploads(files,permanent,path);
        }else {
            return Result.fail("未选择文件");
        }

        if(res == null){
            log.info("多文件上传,请求ip：" + IPUtil.getRealIp(request) + "上传失败!!!");
            return Result.fail("fail");
        }
        log.info("多文件上传,请求ip：" + IPUtil.getRealIp(request) + "上传后的路径：" + Arrays.toString(res));
        return path == null ? Result.success("success") : Result.success("success",res);
    }

    @GetMapping(value = "/download")
    public void download(@RequestParam("path") String path, HttpServletRequest request,HttpServletResponse response){
        log.info("文件下载,请求ip：" + IPUtil.getRealIp(request) + "文件名：" + path);
        downLoadService.download(response,path);
    }
}