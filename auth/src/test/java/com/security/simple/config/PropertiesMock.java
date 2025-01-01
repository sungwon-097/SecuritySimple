package com.security.simple.config;

import com.security.simple.config.SecurityProperties.Jwt;
import com.security.simple.config.SecurityProperties.Jwt.TokenConfig;
import com.security.simple.utils.algorithm.JwtAlgorithm;

import java.time.Duration;
import java.util.List;

import static com.security.simple.config.SecurityProperties.*;

public class PropertiesMock implements Properties {

    public PropertiesMock() {}
    public PropertiesMock(boolean expiredFlag) {
        if (expiredFlag) {
            this.accessConfig = new TokenConfig("123", null, Duration.ZERO, JwtAlgorithm.HS256);
            this.refreshConfig = new TokenConfig("123", null, Duration.ZERO, JwtAlgorithm.HS256);
        }
    }

    private TokenConfig accessConfig = new TokenConfig("123", null, Duration.ofMinutes(1), JwtAlgorithm.HS256);
    private TokenConfig refreshConfig = new TokenConfig("456", null, Duration.ofMinutes(1), JwtAlgorithm.HS256);

    @Override
    public CorsInfo getCors() {
        return null;
    }

    @Override
    public List<String> getNoAuthUrls() {
        return List.of();
    }

    @Override
    public Jwt getJwt() {
        return new Jwt(accessConfig, refreshConfig);
    }

    @Override
    public String getProdProfile() {
        return "";
    }
}