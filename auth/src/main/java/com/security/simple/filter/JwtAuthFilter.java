package com.security.simple.filter;

import com.security.simple.auth.AuthUser;
import com.security.simple.auth.BlackList;
import com.security.simple.config.Properties;
import com.security.simple.filter.exception.TokenException;
import com.security.simple.utils.JwtTokenProvider;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.IOException;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JwtAuthFilter implements Filter {

    private final JwtTokenProvider tokenProvider;
    private final Properties props;
    private final Environment env;
    private final BlackList BlackList;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String path = request.getRequestURI();

        if (props.getNoAuthUrls().contains(path)) {
            chain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("Authorization");

        try {
            if (token == null) {
                throw new RuntimeException("Login required");
            }
            if (env.matchesProfiles(props.getProdProfile())) {
                AuthUser authUser = tokenProvider.getUserFromToken(token);
                if (BlackList.isBlackList(authUser.getUsername())) {
                    throw new RuntimeException("Blacklisted");
                }
            }
            chain.doFilter(request, response);
        } catch (TokenException e) {
            log.debug("JwtAuthFilter : Error occurred during authentication, {}", e.getMessage());
            generateResponse(response, e.getHttpStatus().value(), e.getMessage());
        } catch (RuntimeException e) {
            generateResponse(response, HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }
    }

    private void generateResponse(HttpServletResponse httpServletResponse, int errorCode, String message) throws IOException {
        httpServletResponse.setStatus(errorCode);
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(generateErrorMessage(message));
    }

    private String generateErrorMessage(String message) {
        return String.format("{\"result\": \"fail\", \"message\": \"%s\"}", message);
    }
}
