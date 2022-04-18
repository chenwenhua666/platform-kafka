package com.oumasoft.platform.kafka.interceptor;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.oumasoft.platform.kafka.constants.PlatformConstant;
import com.oumasoft.platform.kafka.constants.StringConstant;
import com.oumasoft.platform.kafka.entity.PlatformResponse;
import com.oumasoft.platform.kafka.exception.PlatformException;
import com.oumasoft.platform.kafka.properties.*;
import com.oumasoft.platform.kafka.tool.utils.PlatformUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

/**
 * @author crystal
 */
public class PlatformServerProtectInterceptor implements HandlerInterceptor {

    @Resource
    private PlatformAuthProperties authProperties;

    @Resource
    private PlatformFoundationProperties foundationProperties;

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        PlatformAnonymousRuleProperties anonymousRuleProperties = foundationProperties.getAnonymousRule();
        if (anonymousRuleProperties != null && anonymousRuleProperties.getEnable()) {
            String uri = request.getServletPath();
            String permissionUri = anonymousRuleProperties.getPermissionUris();
            String[] permissionUris = StringUtils.splitByWholeSeparatorPreserveAllTokens(permissionUri, StringConstant.COMMA);
            if (permissionUris != null && ArrayUtils.isNotEmpty(permissionUris)) {
                PlatformResponse platformResponse = new PlatformResponse();
                for (String u : permissionUris) {
                    if (pathMatcher.match(u, uri)) {
                        String header = request.getHeader(anonymousRuleProperties.getHeader());
                        if (StringUtils.isBlank(header)) {
                            PlatformUtil.makeJsonResponse(response, HttpServletResponse.SC_FORBIDDEN, platformResponse.message("请求头中无授权信息"));
                            return false;
                        }
                        RSA rsaPrivate = new RSA(anonymousRuleProperties.getPrivateKey(),null);
                        String headerValue = rsaPrivate.decryptStr(header, KeyType.PrivateKey);

                        if (StringUtils.isBlank(headerValue) || !headerValue.startsWith(PlatformConstant.TOKEN_BASIC_PREFIX)) {
                            PlatformUtil.makeJsonResponse(response, HttpServletResponse.SC_FORBIDDEN, platformResponse.message("请求头中无client信息"));
                            return false;
                        }
                        String[] tokens = this.extractAndDecodeHeader(headerValue, request);
                        String clientId = tokens[0];
                        String clientSecret = tokens[1];
                        Optional<PlatformClientsProperties> clients = Arrays.stream(authProperties.getClients()).filter(e ->
                                e.getClient().equals(clientId)
                        ).findFirst();
                        if (clients.isPresent() && clients.get().getSecret().equals(clientSecret)) {
                            return true;
                        } else {
                            PlatformUtil.makeJsonResponse(response, HttpServletResponse.SC_FORBIDDEN, platformResponse.message("非法的client信息"));
                            return false;
                        }
                    }
                }
            }
        }
        return true;
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
