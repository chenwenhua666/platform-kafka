package com.oumasoft.platform.kafka.properties;

import com.oumasoft.platform.kafka.constants.EndpointConstant;
import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * @author crystal
 */
@Data
@SpringBootConfiguration
@PropertySource(value = {"classpath:platform-security.properties"})
@ConfigurationProperties(prefix = "platform.security")
public class PlatformSecurityProperties {

    /**
     * 是否开启安全配置
     */
    private Boolean enable = Boolean.TRUE;
    /**
     * 配置需要认证的uri
     */
    private String authUri = EndpointConstant.ALL;
    /**
     * 免认证资源路径
     */
    private String anonymousUris;

}
