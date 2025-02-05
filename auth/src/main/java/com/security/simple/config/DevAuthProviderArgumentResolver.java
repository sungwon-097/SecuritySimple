package com.security.simple.config;

import com.security.simple.auth.AuthUser;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

public class DevAuthProviderArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == AuthUser.class;
    }

    @Override
    public AuthUser resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = webRequest.getHeader("Authorization");
        if (token == null) {
            return null;
        }
        return new AuthUser() {
            @Override
            public String getUsername() {
                return "USER"+token;
            }

            @Override
            public String getRoles() {
                return "USER";
            }

            @Override
            public List<String> getClaims() {
                return List.of();
            }
        };
    }
}
