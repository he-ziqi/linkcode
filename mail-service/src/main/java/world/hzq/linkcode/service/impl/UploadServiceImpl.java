package world.hzq.linkcode.service.impl;

import world.hzq.linkcode.service.UploadService;
import world.hzq.linkcode.util.UploadUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadServiceImpl implements UploadService {
    /**
     * 上传单个
     */
    @Override
    public String upload(MultipartFile file) {
        String msg = UploadUtil.saveFile(file, UploadUtil.getFileDir());
        if(msg == null){ //上传失败
            msg = "fail";
        }
        return msg;
    }

    /**
     * 上传多个
     */
    @Override
    public String[] uploads(MultipartFile[] files) {
        return UploadUtil.saveFile(files, UploadUtil.getFileDir());
    }

    @Override
    public void deleteFile() {
        UploadUtil.deleteFile();
    }
}
