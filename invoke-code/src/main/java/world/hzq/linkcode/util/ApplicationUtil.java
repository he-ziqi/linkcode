package world.hzq.linkcode.util;

import org.springframework.context.ApplicationContext;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/9 08:00
 */
public class ApplicationUtil {
    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext app){
        applicationContext = app;
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }
}
