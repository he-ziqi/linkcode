package world.hzq.linkcode.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    String upload(MultipartFile file,Boolean permanent,String path);
    String[] uploads(MultipartFile[] files,Boolean permanent,String path);
    void deleteFile();
}
