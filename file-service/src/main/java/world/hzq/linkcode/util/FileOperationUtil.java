package world.hzq.linkcode.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class FileOperationUtil {
    private static final String ATTACHMENT_PATH = "upload/files";
    private static final String DIR = "file-center/";

    private FileOperationUtil(){}
    public static File getFileDir(Boolean permanent,String path){
        String filePath = DIR + ATTACHMENT_PATH;
        if(Tools.isNotNull(path)){
            filePath += path;
        }
        //每天创建一个目录
        if(permanent){ //永久保存
            filePath += "/" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "/";
        }else{ //临时保存
            filePath += "/temp/" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "/";
        }
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
        boolean success = FileSystemUtils.deleteRecursively(new File(DIR + ATTACHMENT_PATH + "temp/"));
        log.info("删除临时的文件目录：" + success);
    }

    public static boolean deleteFile(String path){
        File file = new File(path);
        log.info("准备删除文件：{}",path);
        if(!file.exists()){
            return false;
        }
        log.info("删除文件:{}",path);
        return FileSystemUtils.deleteRecursively(file);
    }
}