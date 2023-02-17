package world.hzq.linkcode.util;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import world.hzq.linkcode.entity.User;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hzq
 * @version 1.0
 * @description web工具类
 * @date 2023/2/9 00:13
 */
@Component
public class WebUtil {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * @description 获取当前用户
     * @return: world.hzq.sjm.entity.User
     * @author hzq
     * @date 2023/2/9 00:22
     */
    public User currentUser(){
        String token = getToken();
        if(Tools.isNull(token)){
            return null;
        }
        String userJsonStr = stringRedisTemplate.opsForValue().get(token);
        if(Tools.isNull(userJsonStr)){ //token过期
            return null;
        }
        return JSONObject.parseObject(userJsonStr, User.class);
    }

    /**
     * @description 获取当前用户的token
     * @return: java.lang.String
     * @author hzq
     * @date 2023/2/9 02:14
     */
    public String getToken(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String authorization = request.getHeader("Authorization");
        //错误token
        if (Tools.isNull(authorization) || !authorization.startsWith("bearer ")) {
            return null;
        }
        return authorization.replaceFirst("bearer ", "");
    }

    /**
     * @description 从token的payload中获取用户信息
     * @return: java.util.Map<java.lang.String,java.lang.String>
     * @author hzq
     * @date 2023/2/9 03:41
     */
    public Map<String,String> getTokenInfo(){
        String token = getToken();
        DecodedJWT jwt = JwtUtil.verifyToken(token);
        Map<String,String> info = new HashMap<>();
        info.put("id",String.valueOf(jwt.getClaim("id").asLong()));
        info.put("nickName",jwt.getClaim("nickName").asString());
        return info;
    }
}
