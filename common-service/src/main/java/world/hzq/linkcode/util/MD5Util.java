package world.hzq.linkcode.util;

import org.springframework.util.DigestUtils;

public class MD5Util {
    private MD5Util() {
    }

    /**
     * md5加盐加密
     *
     * @param content 原文
     * @param salt    盐值
     */
    public static String getMd5(String content, String salt) {
        return DigestUtils.md5DigestAsHex((salt + content + salt).getBytes()).toUpperCase();
    }
}
