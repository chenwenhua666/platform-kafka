package com.oumasoft.platform.kafka.properties;

import com.oumasoft.platform.kafka.entity.SecurityUser;
import lombok.Data;

/**
 * @author crystal
 */
@Data
public class PlatformAccountProperties {

    private SecurityUser[] user = {};
}
