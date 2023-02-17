package world.hzq.linkcode.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import world.hzq.linkcode.service.FileInfoManagementService;
import world.hzq.linkcode.util.Result;

/**
 * @author hzq
 * @version 1.0
 * @description 文件信息管理
 * @date 2023/2/10 20:14
 */
@Controller
@RequestMapping("/info")
@Api(tags = "文件信息管理")
public class FileInfoManagementController {

    @Autowired
    private FileInfoManagementService fileInfoManagementService;

    @ApiOperation("删除文件")
    @DeleteMapping("/delete")
    //@OnlyInternalCall
    public Result<String> deleteFile(@ApiParam("文件路径") @RequestParam("path") String path){
        return fileInfoManagementService.deleteFile(path);
    }
}
