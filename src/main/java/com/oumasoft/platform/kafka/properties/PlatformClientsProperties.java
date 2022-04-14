package com.oumasoft.platform.kafka.properties;

import lombok.Data;

/**
 * @author crystal
 */
@Data
public class PlatformClientsProperties {

    private String client;
    private String secret;
    private String grantType = "password,authorization_code,refresh_token";
    private String scope = "all";
    private int accessTokenValiditySeconds = 60 * 60 * 24;
    private int refreshTokenValiditySeconds = 60 * 60 * 24 * 7;
    private Boolean autoApprove = Boolean.TRUE;
    private String redirectUris;

}
