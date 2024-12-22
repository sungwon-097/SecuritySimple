package com.security.simple.config;

import com.security.simple.config.SecurityProperties.CorsInfo;
import com.security.simple.config.SecurityProperties.Jwt;

import java.util.List;

public interface Properties {

    CorsInfo getCors();

    List<String> getNoAuthUrls();

    Jwt getJwt();
}
