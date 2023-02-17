package world.hzq.linkcode.service.impl;

import world.hzq.linkcode.service.UploadService;
import world.hzq.linkcode.util.FileOperationUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadServiceImpl implements UploadService {
    //同步es信息

    /**
     * 上传单个
     */
    @Override
    public String upload(MultipartFile file,Boolean permanent,String path) {
        String msg = FileOperationUtil.saveFile(file, FileOperationUtil.getFileDir(permanent,path));
        if(msg == null){ //上传失败
            msg = "fail";
        }
        return msg;
    }

    /**
     * 上传多个
     */
    @Override
    public String[] uploads(MultipartFile[] files,Boolean permanent,String path) {
        return FileOperationUtil.saveFile(files, FileOperationUtil.getFileDir(permanent,path));
    }

    @Override
    public void deleteFile() {
        FileOperationUtil.deleteFile();
    }
}
