package com.security.project.mock;

import com.security.simple.config.Properties;
import com.security.simple.config.SecurityProperties.Jwt.TokenConfig;
import com.security.simple.utils.algorithm.JwtAlgorithm;

import java.time.Duration;
import java.util.List;

import static com.security.simple.config.SecurityProperties.CorsInfo;
import static com.security.simple.config.SecurityProperties.Jwt;

public class PropertiesMock implements Properties {

    private final TokenConfig accessConfig = new TokenConfig("privateKey", null, Duration.ofMinutes(1), JwtAlgorithm.HS256);
    private final TokenConfig refreshConfig = new TokenConfig("publicKey", null, Duration.ofMinutes(1), JwtAlgorithm.HS256);

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
        return "local";
    }
}
