package com.security.simple.config;

import com.security.simple.utils.algorithm.JwtAlgorithm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.time.Duration;
import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "security-properties")
public class SecurityProperties implements Properties {

    private CorsInfo cors;
    private List<String> noAuthUrls;
    private Jwt jwt;
    private String prodProfile = "prod";

    @Getter
    @Setter
    @AllArgsConstructor
    public static class CorsInfo {
        private List<String> origins;
        private List<HttpMethod> methods;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Jwt {
        private TokenConfig access;
        private TokenConfig refresh;

        @Getter
        @Setter
        @AllArgsConstructor
        public static class TokenConfig {
            private String privateKey;
            private String publicKey;
            private Duration expiringTime;
            private JwtAlgorithm algorithm;
        }
    }
}
