package com.oumasoft.platform.kafka.handler;

import com.oumasoft.platform.kafka.entity.PlatformResponse;
import com.oumasoft.platform.kafka.tool.utils.PlatformUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author crystal
 */
@Component
public class PlatformWebLoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException exception) throws IOException {
        String message;
        if (exception instanceof BadCredentialsException) {
            message = "用户名或密码错误！";
        } else if (exception instanceof LockedException) {
            message = "用户已被锁定！";
        } else {
            message = "认证失败";
        }
        PlatformResponse platformResponse = new PlatformResponse().message(message);
        PlatformUtil.makeFailureResponse(httpServletResponse, platformResponse);
    }
}
