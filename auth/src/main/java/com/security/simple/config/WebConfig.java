package com.security.simple.config;

import com.security.simple.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final Environment environment;
    private final JwtTokenProvider provider;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        if (List.of("dev", "test").contains(environment.getActiveProfiles())){
            resolvers.add(new DevAuthProviderArgumentResolver());
        } else {
            resolvers.add(new AuthProviderArgumentResolver(provider));
        }
    }
}
