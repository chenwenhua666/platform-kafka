package com.oumasoft.platform.kafka.handler;

import com.oumasoft.platform.kafka.entity.PlatformResponse;
import com.oumasoft.platform.kafka.tool.utils.PlatformUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author crystal
 */
public class PlatformAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        PlatformResponse platformResponse = new PlatformResponse();
        PlatformUtil.makeJsonResponse(response, HttpServletResponse.SC_FORBIDDEN, platformResponse.message("没有权限访问该资源"));
    }
}
