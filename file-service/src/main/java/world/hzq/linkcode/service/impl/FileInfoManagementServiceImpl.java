package world.hzq.linkcode.service.impl;

import org.springframework.stereotype.Service;
import world.hzq.linkcode.service.FileInfoManagementService;
import world.hzq.linkcode.util.FileOperationUtil;
import world.hzq.linkcode.util.Result;

/**
 * @author hzq
 * @version 1.0
 * @description 文件信息管理服务实现
 * @date 2023/2/10 20:20
 */
@Service
public class FileInfoManagementServiceImpl implements FileInfoManagementService {
    @Override
    public Result<String> deleteFile(String path) {
        boolean success = FileOperationUtil.deleteFile(path);
        return success ? Result.success("删除成功") : Result.fail("删除失败,文件不存在");
    }
}
