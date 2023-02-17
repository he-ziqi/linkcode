package world.hzq.linkcode.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hzq
 * @version 1.0
 * @description jwt工具类
 * @date 2023/2/7 14:19
 */
public class JwtUtil {
    /** 过期时间，单位：秒，默认最长七天过期 **/
    private static final long EXPIRATION = 24 * 3600 * 7;

    /** 密钥，一般长度较长，内容较复杂 **/
    private static final String secret = "bbbab96e-133d-216d-0d7d-3b83e147cefa";

    /**
     * @description 创建token
     * @date 20:49 2022/3/31
     **/
    public static String createToken(Map<String, String> claimMap) {
        // 当前时间戳加上设定的毫秒数（1秒 == 1000毫秒）
        Date expiration = new Date(System.currentTimeMillis() + EXPIRATION * 1000);
        // 设置JWT头部
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        // 创建token
        JWTCreator.Builder builder = JWT.create();

        //使用Lambda创建payload
        claimMap.forEach(builder::withClaim);

        // 添加头部，可省略保持默认，默认即map中的键值对
        return builder.withHeader(map)
                // 设置过期时间
                .withExpiresAt(expiration)
                // 设置签名解码算法
                .sign(Algorithm.HMAC256(secret));
    }

    /**
     * @description 验证token
     * @date 23:36 2022/3/31
     **/
    public static DecodedJWT verifyToken(String token) {
        return JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
    }
}
