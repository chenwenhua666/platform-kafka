package com.oumasoft.platform.kafka.controller;

import com.oumasoft.platform.kafka.constants.StringConstant;
import com.oumasoft.platform.kafka.entity.PlatformResponse;
import com.oumasoft.platform.kafka.properties.PlatformAuthProperties;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Principal;

import static com.oumasoft.platform.kafka.constants.PlatformConstant.TOKEN_BEARER_PREFIX;
import static com.oumasoft.platform.kafka.constants.PlatformConstant.TOKEN_HEADER;

/**
 * @author crystal
 */
@Controller
@RequiredArgsConstructor
public class SecurityController {

    private final ConsumerTokenServices consumerTokenServices;
    private final PlatformAuthProperties authProperties;

    @ResponseBody
    @GetMapping("auth/principal")
    public Principal currentUser(Principal principal) {
        return principal;
    }

    @ResponseBody
    @GetMapping("auth/authentication")
    public Authentication currentUser(Authentication authentication) {
        return authentication;
    }

    @ResponseBody
    @GetMapping("auth/user")
    public Object authentication(HttpServletRequest request) {
        String header = request.getHeader(TOKEN_HEADER);
        String token = StringUtils.substringAfter(header, TOKEN_BEARER_PREFIX);
        return Jwts.parser().setSigningKey(authProperties.getJwtAccessKey().getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token).getBody();
    }

    @RequestMapping("login")
    public String login() {
        return "login";
    }

    @ResponseBody
    @DeleteMapping("signout")
    public PlatformResponse signout(HttpServletRequest request, @RequestHeader(TOKEN_HEADER) String token) {
        token = StringUtils.replace(token, TOKEN_BEARER_PREFIX, StringConstant.EMPTY);
        consumerTokenServices.revokeToken(token);
        return new PlatformResponse().message("signout");
    }
}
