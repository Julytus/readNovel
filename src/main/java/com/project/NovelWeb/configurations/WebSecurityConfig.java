package com.project.NovelWeb.configurations;

import com.project.NovelWeb.filters.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@RequiredArgsConstructor
public class WebSecurityConfig {
    @Value("${api.prefix}")
    private String apiPrefix;
    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
         http
                 .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)

                 .authorizeHttpRequests(requests -> requests
                         .requestMatchers(
                                 String.format("%s/auth/login", apiPrefix),
                                 String.format("%s/auth/register", apiPrefix)).permitAll()

                         .requestMatchers(GET,
                                 String.format("%s/user/detail", apiPrefix)).permitAll()

                         .requestMatchers(PUT,
                                 String.format("%s/user/detail", apiPrefix)).permitAll()

                         .requestMatchers(POST,
                                 String.format("%s/user/avatar/**", apiPrefix)).permitAll()

                         .requestMatchers(GET,
                                 String.format("%s/novel/**", apiPrefix)).permitAll()
                         .anyRequest().authenticated())

                 .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
