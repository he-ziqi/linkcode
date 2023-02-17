package world.hzq.linkcode.util;

import java.util.Date;

/**
 * 时间工具类
 */
public class TimeUtil {
    private TimeUtil(){}

    /**
     * 计算给定的两个时间间隔多少秒
     * @param past 过去时间
     * @param now 当前时间
     * @return 时间间隔
     */
    public static Long timeInterval(Date past,Date now){
        return (now.getTime() - past.getTime()) / 1000;
    }

    /**
     * 计算过去的时间距离当前过去了多少秒
     * @param past
     * @return
     */
    public static Long timeInterval(Date past){
        return (new Date().getTime() - past.getTime()) / 1000;
    }

    /**
     * @description 获取指定时间的后day天的时间
     * @param: time
     * @param: day
     * @return: java.util.Date
     * @author hzq
     * @date 2023/2/12 21:41
     */
    public static Date afterTime(Date time,Integer day){
        long afterTime = time.getTime() + day * 24 * 3600 * 1000;
        return new Date(afterTime);
    }

    /**
     * @description 获取指定时间的前day天的时间
     * @param: time
     * @param: day
     * @return: java.util.Date
     * @author hzq
     * @date 2023/2/12 21:44
     */
    public static Date beforeTime(Date time,Integer day){
        long beforeTime = time.getTime() - day * 24 * 3600 * 1000;
        return new Date(beforeTime);
    }

    /**
     * @description time1是否大于time2
     * @param: time1
     * @param: time2
     * @return: boolean
     * @author hzq
     * @date 2023/2/13 00:38
     */
    public static boolean thanTime(Date time1,Date time2){
        return time1.getTime() > time2.getTime();
    }

}
