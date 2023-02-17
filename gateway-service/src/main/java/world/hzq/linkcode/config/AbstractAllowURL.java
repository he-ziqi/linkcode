package world.hzq.linkcode.config;

import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description 抽象允许url
 * @date 2023/2/13 16:19
 */
public abstract class AbstractAllowURL {
    public boolean allow(String path,List<String> urlList){
        for (String url : urlList) {
            //全量匹配
            if (url.equals(path)) {
                return true;
            }
            //通配匹配
            if (url.endsWith("/**")) {
                int lastIndexOf = url.lastIndexOf("/**");
                if (path.startsWith(url.substring(0, lastIndexOf))) {
                    return true;
                }
            }
        }
        return false;
    }
}
