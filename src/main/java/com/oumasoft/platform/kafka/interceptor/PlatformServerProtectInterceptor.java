package com.oumasoft.platform.kafka.interceptor;

import com.oumasoft.platform.kafka.constants.PlatformConstant;
import com.oumasoft.platform.kafka.entity.PlatformResponse;
import com.oumasoft.platform.kafka.exception.PlatformException;
import com.oumasoft.platform.kafka.properties.PlatformAuthProperties;
import com.oumasoft.platform.kafka.properties.PlatformClientsProperties;
import com.oumasoft.platform.kafka.tool.utils.PlatformUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.Base64Utils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

/**
 * @author crystal
 */
public class PlatformServerProtectInterceptor implements HandlerInterceptor {

    private PlatformAuthProperties properties;

    public void setProperties(PlatformAuthProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws IOException {
        if (properties == null || !properties.getAuthorizeAll()) {
            return true;
        }
        String header = request.getHeader(PlatformConstant.TOKEN_HEADER);
        if (header == null || !header.startsWith(PlatformConstant.TOKEN_BASIC_PREFIX)) {
            throw new PlatformException("请求头中无client信息");
        }

        String[] tokens = this.extractAndDecodeHeader(header, request);
        String clientId = tokens[0];
        String clientSecret = tokens[1];
        Optional<PlatformClientsProperties> clients = Arrays.stream(properties.getClients()).filter(e ->
                e.getClient().equals(clientId)
        ).findFirst();
        if (clients.isPresent() && clients.get().getSecret().equals(clientSecret)) {
            return true;
        } else {
            PlatformResponse platformResponse = new PlatformResponse();
            PlatformUtil.makeJsonResponse(response, HttpServletResponse.SC_FORBIDDEN, platformResponse.message("非法的client信息"));
            return false;
        }

    }

    private String[] extractAndDecodeHeader(String header, HttpServletRequest request) {
        byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);

        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(base64Token);
        } catch (IllegalArgumentException var7) {
            throw new PlatformException("Failed to decode basic authentication token");
        }

        String token = new String(decoded, StandardCharsets.UTF_8);
        int delim = token.indexOf(":");
        if (delim == -1) {
            throw new PlatformException("Invalid basic authentication token");
        } else {
            return new String[]{token.substring(0, delim), token.substring(delim + 1)};
        }
    }


}
