package com.project.NovelWeb.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@RequiredArgsConstructor
public class SecurityConfig {
    @Value("${api.prefix}")
    private String apiPrefix;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
         http
                 .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                 .authorizeHttpRequests(requests -> requests
                         .requestMatchers(
                                 String.format("%s/auth/**", apiPrefix)).permitAll()
                         .requestMatchers(GET,
                                 String.format("%s/users/detail", apiPrefix)).permitAll()
                         .requestMatchers(PUT,
                                 String.format("%s/users/detail", apiPrefix)).permitAll()
                         .requestMatchers(POST,
                                 String.format("%s/users/avatar/**", apiPrefix)).permitAll()
                         .requestMatchers(GET,
                                 String.format("%s/novels/**", apiPrefix)).permitAll()
                         .anyRequest().authenticated())
                 .oauth2ResourceServer(oauth2 -> oauth2
                         .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                 )
                 .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("role");
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
