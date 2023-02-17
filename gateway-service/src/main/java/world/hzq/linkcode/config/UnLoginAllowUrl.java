package world.hzq.linkcode.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description 未登录允许的url
 * @date 2023/2/8 20:47
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Component
@ConfigurationProperties(prefix = "interceptor.config.unlogin")
public class UnLoginAllowUrl extends AbstractAllowURL{
    private List<String> allow;
    private List<String> allowInterfaceCenter;
}
