package world.hzq.linkcode.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.*;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/1 23:22
 */
@Configuration
public class SwaggerConfig implements WebMvcConfigurer {
    //配置了Swagger的Docket的bean实例
    //enable是否启动swagger，如果为False则Swagger不能在浏览器访问
    @Value("${swagger.enable}")
    private boolean enableSwagger;

    @Bean
    public Docket docket() {
        Set<String> set = new HashSet<>();
        set.add("https");
        set.add("http");
        return new Docket(DocumentationType.SWAGGER_2).pathMapping("/")
                .enable(enableSwagger)//定义是否开启swagger，false为关闭，可以通过变量控制
                .apiInfo(apiInfo())//将api的元信息设置为包含在json ResourceListing响应中。
                .select()
                .apis(RequestHandlerSelectors.basePackage("world.hzq.linkcode"))
                //paths()过滤什么路径
                .paths(PathSelectors.any())
                .build()
                .protocols(set)// 支持的通讯协议集合
                .securitySchemes(securitySchemes())// 授权信息设置，必要的header token等认证信息
                .securityContexts(securityContexts());// 授权信息全局应用
    }
    //作者信息
    Contact contact = new Contact("hzq","world.hzq.linkcode","1440287357@qq.com");

    //配置Swagger 信息 = ApiInfo
    private ApiInfo apiInfo()
    {
        return new ApiInfo("实践教学平台(linkcode)API接口文档",
                "愿永无bug",
                "1.0",
                "world.hzq.linkcode",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList<>());
    }
    /**
     * 设置授权信息
     */
    private List<SecurityScheme> securitySchemes()
    {
        List<ApiKey> result = new ArrayList<>();
        ApiKey apiKey = new ApiKey("Authorization","Authorization" ,"Header" );
        result.add(apiKey);
        return Collections.singletonList(apiKey);
    }
    /**
     * 授权信息全局应用
     */
    private List<SecurityContext> securityContexts() {
        return Collections.singletonList(
                SecurityContext.builder()
                        .securityReferences(Collections.singletonList(new SecurityReference("Authorization", new AuthorizationScope[]{new AuthorizationScope("global", "Authorization")})))
                        .build()
        );
    }

}
