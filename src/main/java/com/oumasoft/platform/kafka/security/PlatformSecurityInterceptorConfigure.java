package com.oumasoft.platform.kafka.security;

import com.oumasoft.platform.kafka.interceptor.PlatformServerProtectInterceptor;
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

    @Bean
    public HandlerInterceptor platformServerProtectInterceptor() {
        return new PlatformServerProtectInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(platformServerProtectInterceptor());
    }


}
