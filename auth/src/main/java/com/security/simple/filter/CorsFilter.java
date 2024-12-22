package com.security.simple.filter;

import com.security.simple.config.SecurityProperties;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class CorsFilter implements Filter {

    private final SecurityProperties properties;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        var origins = String.join(",", properties.getCors().getOrigins());
        var methods = properties.getCors().getMethods().stream().map(HttpMethod::toString).collect(Collectors.joining(","));

        response.addHeader("Access-Control-Allow-Origin", origins);
        response.setHeader("Access-Control-Allow-Methods", methods);
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(request, response);
    }
}
