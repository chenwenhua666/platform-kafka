package com.oumasoft.platform.kafka.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author crystal
 */
@Data
public class SecurityUser implements Serializable {

    private String username;
    private String password;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;
    private String permissions;
}
