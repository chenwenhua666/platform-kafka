package com.oumasoft.platform.kafka.security;

import com.oumasoft.platform.kafka.handler.PlatformAccessDeniedHandler;
import com.oumasoft.platform.kafka.handler.PlatformAuthExceptionEntryPoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

/**
 * @author crystal
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class PlatformSecurityAutoConfigure extends GlobalMethodSecurityConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "accessDeniedHandler")
    public PlatformAccessDeniedHandler accessDeniedHandler() {
        return new PlatformAccessDeniedHandler();
    }

    @Bean
    @ConditionalOnMissingBean(name = "authenticationEntryPoint")
    public PlatformAuthExceptionEntryPoint authenticationEntryPoint() {
        return new PlatformAuthExceptionEntryPoint();
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return new OAuth2MethodSecurityExpressionHandler();
    }
}
