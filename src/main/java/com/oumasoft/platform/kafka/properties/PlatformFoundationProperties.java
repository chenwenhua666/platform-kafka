package com.oumasoft.platform.kafka.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author crystal
 */
@Data
@Component
@ConfigurationProperties(prefix = "platform.foundation")
public class PlatformFoundationProperties {

    private PlatformAnonymousRuleProperties anonymousRule = new PlatformAnonymousRuleProperties();

}
