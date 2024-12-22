package com.security.simple.filter;

import com.security.simple.auth.AuthContext;
import com.security.simple.auth.AuthUser;
import com.security.simple.config.Properties;
import com.security.simple.filter.exception.TokenException;
import com.security.simple.utils.JwtTokenProvider;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class JwtAuthFilter implements Filter {

    private final JwtTokenProvider tokenProvider;
    private final Properties properties;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String path = request.getRequestURI();

        if (properties.getNoAuthUrls() != null && properties.getNoAuthUrls().contains(path)) {
            chain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("Authorization");
        if (token != null) {
            try {
                AuthUser authUser = tokenProvider.getUserFromToken(token);
                if (authUser != null) {
                    AuthContext.setUser(authUser);
                }
            } catch (TokenException e) {
                response.setStatus(e.getHttpStatus().value());
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(generateErrorResponse(e.getMessage()));
                return;
            }
        }
        chain.doFilter(request, response);
        AuthContext.clear();
    }

    private String generateErrorResponse(String message) {
        return String.format("{\"result\": \"fail\", \"message\": \"%s\"}", message);
    }
}
