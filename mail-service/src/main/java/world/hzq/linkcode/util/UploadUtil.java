package world.hzq.linkcode.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
@Slf4j
public class UploadUtil {
    private static final String ATTACHMENT_PATH = "mail/files";
    private static final String DIR = "file-center/";
    private UploadUtil(){}
    public static File getFileDir(){
        String filePath = new String(DIR + ATTACHMENT_PATH);
        File file = new File(filePath);
        if(!file.exists()){
            file.mkdirs();
        }
        return file;
    }

    public static String saveFile(MultipartFile file, File dirFile){
        String filename = file.getOriginalFilename();
        String filePath = dirFile.getAbsolutePath() + File.separator + new Date().getTime() + filename;
        File newFile = new File(filePath);
        try {
            file.transferTo(newFile);
        } catch (IOException e) {
            filePath = null;
            e.printStackTrace();
        }
        return filePath;
    }

    public static String[] saveFile(MultipartFile[] files,File dirFile){
        String[] res = new String[files.length];
        int index = 0;
        for (MultipartFile file : files) {
            String msg = saveFile(file, dirFile);
            if(msg == null){
                res = null;
                break;
            }
            res[index++] = msg;
        }
        return res;
    }

    public static void deleteFile(){
        boolean success = FileSystemUtils.deleteRecursively(new File(DIR + ATTACHMENT_PATH));
        log.info("删除文件上传目录的文件：" + success);
    }
}