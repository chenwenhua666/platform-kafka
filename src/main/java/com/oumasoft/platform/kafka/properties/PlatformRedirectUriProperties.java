package com.oumasoft.platform.kafka.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

/**
 * @author crystal
 */
@Data
@SpringBootConfiguration
//@PropertySource(value = {"file:/opt/kafka/properties/platform-redirect-uri.properties"})
@PropertySource(value = {"classpath:platform-redirect-uri.properties"})
@ConfigurationProperties(prefix = "platform.redirect")
public class PlatformRedirectUriProperties {

    private Map<String, String> wangbao;
    private Map<String, String> wangbaoHigh;
    private Map<String, String> zhengshu;

}