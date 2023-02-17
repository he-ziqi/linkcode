package world.hzq.linkcode.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author hzq
 * @version 1.0
 * @description 角色允许的url
 * @date 2023/2/13 16:14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Component
@ConfigurationProperties(prefix = "interceptor.config.role")
public class RoleAllowUrl extends AbstractAllowURL{
    List<String> notAllowTeacher;
    List<String> notAllowStudent;
}
