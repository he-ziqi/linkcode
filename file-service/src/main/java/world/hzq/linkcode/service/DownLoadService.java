package world.hzq.linkcode.service;

import world.hzq.linkcode.util.Result;

import javax.servlet.http.HttpServletResponse;

public interface DownLoadService {
    Result<String> download(HttpServletResponse response, String path);
}
