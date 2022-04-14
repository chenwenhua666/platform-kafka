package com.oumasoft.platform.kafka.security;

import com.oumasoft.platform.kafka.constants.EndpointConstant;
import com.oumasoft.platform.kafka.constants.StringConstant;
import com.oumasoft.platform.kafka.handler.PlatformAccessDeniedHandler;
import com.oumasoft.platform.kafka.handler.PlatformAuthExceptionEntryPoint;
import com.oumasoft.platform.kafka.properties.PlatformSecurityProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * @author crystal
 */
@Configuration
@EnableResourceServer
@RequiredArgsConstructor
public class PlatformResourceServerConfigure extends ResourceServerConfigurerAdapter {

    private final PlatformSecurityProperties properties;
    private final PlatformAccessDeniedHandler accessDeniedHandler;
    private final PlatformAuthExceptionEntryPoint authenticationEntryPoint;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        if (properties == null) {
            permitAll(http);
            return;
        }
        String[] anonymousUris = StringUtils.splitByWholeSeparatorPreserveAllTokens(properties.getAnonymousUris(), StringConstant.COMMA);
        if (ArrayUtils.isEmpty(anonymousUris)) {
            anonymousUris = new String[]{};
        }
        if (ArrayUtils.contains(anonymousUris, EndpointConstant.ALL)) {
            permitAll(http);
            return;
        }
        http.csrf().disable()
                .requestMatchers().antMatchers(properties.getAuthUri())
                .and()
                .authorizeRequests()
                .antMatchers(anonymousUris).permitAll()
                .antMatchers(properties.getAuthUri()).authenticated()
                .and()
                .httpBasic();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.authenticationEntryPoint(authenticationEntryPoint);
        resources.accessDeniedHandler(accessDeniedHandler);
    }

    private void permitAll(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().anyRequest().permitAll();
    }
}
