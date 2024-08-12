package com.project.NovelWeb.configurations;

import com.project.NovelWeb.services.UserService;
import com.project.NovelWeb.utils.localization.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component("userDetailsService")
@RequiredArgsConstructor
public class UserDetailsCustom implements UserDetailsService {
    private final Logger log = LoggerFactory.getLogger(UserDetailsCustom.class);
    private final UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Authenticating {}", username);

        com.project.NovelWeb.models.entities.User user = userService.getUserByEmai(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username/password");
        }
        if (!ValidationUtils.isValidEmail(username)) {
            throw new UsernameNotFoundException("Invalid email format");
        }
        return new User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName())));
    }
}
