package world.hzq.linkcode.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * @author hzq
 * @version 1.0
 * @description 跨域配置
 * @date 2023/2/3 16:16
 */
@Configuration
public class CorsConfig {
    /**
     * 配置跨域
     * @return
     */
    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // cookie跨域
        config.setAllowCredentials(Boolean.TRUE);
        config.addAllowedMethod("*");
        //SpringBoot升级2.4.0之后，跨域配置中的.allowedOrigins不再可用 要替换为addAllowedOriginPattern
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        // 配置前端js允许访问的自定义响应头
        config.addExposedHeader("setToken");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }


}
