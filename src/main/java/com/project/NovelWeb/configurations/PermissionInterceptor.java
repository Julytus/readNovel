package com.project.NovelWeb.configurations;

import com.project.NovelWeb.exceptions.IdInvalidException;
import com.project.NovelWeb.models.entities.Permission;
import com.project.NovelWeb.models.entities.Role;
import com.project.NovelWeb.models.entities.User;
import com.project.NovelWeb.services.UserService;
import com.project.NovelWeb.utils.jwt.JwtTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.List;

public class PermissionInterceptor implements HandlerInterceptor {
    @Value("${api.prefix}")
    private String apiPrefix;

    @Autowired
    UserService userService;

    @Override
    @Transactional
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws Exception {

        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();

        // check permission
        String email = JwtTokenUtils.getCurrentUserLogin().isPresent()
                 ? JwtTokenUtils.getCurrentUserLogin().get()
                : "";
        if (!email.isEmpty()) {
            User user = this.userService.getUserByEmai(email);
            if (user != null) {
                Role role = user.getRole();
                if (role != null) {
                    List<Permission> permissions = role.getPermissions();
                    boolean isAllow = permissions.stream().anyMatch(item -> (apiPrefix + item.getApiPath()).equals(path)
                            && item.getMethod().equals(httpMethod));

                    if (!isAllow) {
                        throw new IdInvalidException("Bạn không có quyền truy cập endpoint này.");
                    }
                } else {
                    throw new IdInvalidException("Bạn không có quyền truy cập endpoint này.");
                }
            }
        }

        return true;
    }
}
