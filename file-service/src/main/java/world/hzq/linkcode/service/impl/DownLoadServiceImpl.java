package world.hzq.linkcode.service.impl;

import world.hzq.linkcode.service.DownLoadService;
import org.springframework.stereotype.Service;
import world.hzq.linkcode.util.Result;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@Service
public class DownLoadServiceImpl implements DownLoadService {
    @Override
    public Result<String> download(HttpServletResponse response, String path){
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        // 下载文件能正常显示中文
        BufferedInputStream bi = null;
        OutputStream os = null;
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(path, "UTF-8"));
            bi = new BufferedInputStream(new FileInputStream(path));
            os = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int readCount = -1;
            while((readCount = bi.read(buffer)) != -1){
                os.write(buffer,0,readCount);
            }
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail("fail");
        } finally {
            try {
                if(bi != null){
                    bi.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Result.success("success");
    }
}
