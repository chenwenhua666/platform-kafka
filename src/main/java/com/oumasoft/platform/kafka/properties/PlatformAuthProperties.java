package com.oumasoft.platform.kafka.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * @author crystal
 */
@Data
@SpringBootConfiguration
@PropertySource(value = {"classpath:platform-auth.properties"})
@ConfigurationProperties(prefix = "platform.auth")
public class PlatformAuthProperties {

    private PlatformClientsProperties[] clients = {};

    /**
     * JWT加签密钥
     */
    private String jwtAccessKey;

}
