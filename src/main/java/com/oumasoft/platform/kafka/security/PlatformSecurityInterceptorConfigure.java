package com.oumasoft.platform.kafka.security;

import com.oumasoft.platform.kafka.interceptor.PlatformServerProtectInterceptor;
import com.oumasoft.platform.kafka.properties.PlatformAuthProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author crystal
 */
@Configuration
public class PlatformSecurityInterceptorConfigure implements WebMvcConfigurer {

    private PlatformAuthProperties properties;

    @Autowired
    public void setProperties(PlatformAuthProperties properties) {
        this.properties = properties;
    }

    @Bean
    public HandlerInterceptor platformServerProtectInterceptor() {
        PlatformServerProtectInterceptor platformServerProtectInterceptor = new PlatformServerProtectInterceptor();
        platformServerProtectInterceptor.setProperties(properties);
        return platformServerProtectInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(platformServerProtectInterceptor());
    }


}
