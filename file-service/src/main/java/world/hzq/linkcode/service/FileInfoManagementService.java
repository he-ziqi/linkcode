package world.hzq.linkcode.service;

import world.hzq.linkcode.util.Result;

/**
 * @author hzq
 * @version 1.0
 * @description 文件信息管理服务
 * @date 2023/2/10 20:19
 */
public interface FileInfoManagementService {

    /**
     * @description 根据路径删除文件
     * @param: path 文件路径
     * @return: world.hzq.sjm.util.Result<java.lang.String>
     * @author hzq
     * @date 2023/2/10 20:19
     */
    Result<String> deleteFile(String path);
}
