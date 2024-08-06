package com.project.NovelWeb.configurations;

import com.project.NovelWeb.filters.JwtTokenFilter;
import com.project.NovelWeb.models.entities.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
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
                                 String.format("%s/novel/**", apiPrefix)).permitAll()
                         .requestMatchers(GET,
                                 String.format("%s/user/**", apiPrefix)).hasRole(Role.ADMIN)
                         .requestMatchers(POST,
                                 String.format("%s/novel/**", apiPrefix)).hasRole(Role.ADMIN)
                         .requestMatchers(DELETE,
                                 String.format("%s/novel/**", apiPrefix)).hasRole(Role.ADMIN)
                         .requestMatchers(PUT,
                                 String.format("%s/novel/**", apiPrefix)).hasRole(Role.ADMIN)
                         .requestMatchers(POST,
                                 String.format("%s/chapter/**", apiPrefix)).hasAnyRole(Role.POSTER, Role.ADMIN)
                         .requestMatchers(DELETE,
                                 String.format("%s/chapter/**", apiPrefix)).hasAnyRole(Role.POSTER, Role.ADMIN)
                         .anyRequest().authenticated())

                 .csrf(AbstractHttpConfigurer::disable);
        http.cors(httpSecurityCorsConfigurer -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(List.of("http://localhost:4200/"));
            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
            configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
            configuration.setExposedHeaders(List.of("x-auth-token"));
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            httpSecurityCorsConfigurer.configurationSource(source);
        });
        return http.build();
    }
}
