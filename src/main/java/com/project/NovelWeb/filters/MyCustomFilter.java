//package com.project.NovelWeb.filters;
//
//import com.project.NovelWeb.models.entities.User;
//import com.project.NovelWeb.utils.JwtTokenUtils;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.NonNull;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
//import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//@RequiredArgsConstructor
//@Component
//public class MyCustomFilter extends OncePerRequestFilter {
//    private final JwtTokenUtils jwtTokenUtils;
//    private final UserDetailsService userDetailsService;
//    BearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();
//    @Override
//    protected void doFilterInternal(@NonNull HttpServletRequest request,
//                                    @NonNull HttpServletResponse response,
//                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
//        String token = bearerTokenResolver.resolve(request);
//        String email = jwtTokenUtils.extractEmail(token);
//        User user = (User) userDetailsService.loadUserByUsername(email);
//
//        if (!jwtTokenUtils.validateToken(token, user)) {
//            throw new BadCredentialsException("Invalid token");
//        }
//        filterChain.doFilter(request, response);
//    }
//}