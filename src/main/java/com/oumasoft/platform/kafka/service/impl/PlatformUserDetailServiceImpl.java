package com.oumasoft.platform.kafka.service.impl;

import com.oumasoft.platform.kafka.entity.SecurityUser;
import com.oumasoft.platform.kafka.properties.PlatformFoundationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author crystal
 */
@Service
@RequiredArgsConstructor
public class PlatformUserDetailServiceImpl implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final PlatformFoundationProperties foundationProperties;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SecurityUser[] users = foundationProperties.getAccount().getUser();
        Optional<SecurityUser> optionalUser = Arrays.stream(users).filter(u ->
                username.equals(u.getUsername())
        ).findFirst();
        if (optionalUser.isPresent()) {
            SecurityUser user = optionalUser.get();
            return new User(user.getUsername(), user.getPassword(), user.isEnabled(),
                    user.isAccountNonExpired(), user.isCredentialsNonExpired(),
                    user.isAccountNonLocked(), AuthorityUtils.commaSeparatedStringToAuthorityList(user.getPermissions()));
        } else {
            throw new UsernameNotFoundException("");
        }
    }
}
