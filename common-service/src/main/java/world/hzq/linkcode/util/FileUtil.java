package world.hzq.linkcode.util;

import java.io.File;
import java.io.IOException;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/9 08:38
 */
public class FileUtil {
    private FileUtil(){}

    /**
     * @description 创建文件
     * @param: filePath 文件目录
     * @author hzq
     * @date 2023/2/9 08:39
     */
    public static void createFile(String filePath){
        File file = new File(filePath);
        if(!file.exists()){
            //创建文件夹
            int lastIndexOf = filePath.lastIndexOf(File.separator);
            File dirFile = new File(filePath.substring(0,lastIndexOf));
            if(!dirFile.exists()){
                dirFile.mkdirs();
            }
            File sourceFile = new File(filePath);
            if(!sourceFile.exists()){
                //文件不存在则创建文件
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
