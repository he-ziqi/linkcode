package world.hzq.linkcode.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    String upload(MultipartFile file);
    String[] uploads(MultipartFile[] files);
    void deleteFile();
}
