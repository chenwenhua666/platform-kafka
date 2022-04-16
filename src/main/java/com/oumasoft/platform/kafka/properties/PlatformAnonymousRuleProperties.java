package com.oumasoft.platform.kafka.properties;

import lombok.Data;

/**
 * @author crystal
 */
@Data
public class PlatformAnonymousRuleProperties {

    /**
     * 启用
     */
    private Boolean enable;

    /**
     * 请求头
     */
    private String header;

    /**
     * 校验的uri
     */
    private String permissionUris;

    /**
     * 公钥
     */
    private String publicKey;

    /**
     * 私钥
     */
    private String privateKey;
}
