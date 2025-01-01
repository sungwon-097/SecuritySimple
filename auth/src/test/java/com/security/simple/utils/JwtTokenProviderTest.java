package com.security.simple.utils;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.security.simple.auth.AuthUser;
import com.security.simple.config.PropertiesMock;
import com.security.simple.filter.exception.ExpiredException;
import com.security.simple.filter.exception.InvalidSignatureException;
import com.security.simple.filter.exception.MalformedTokenException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    final JwtTokenProvider provider = new JwtTokenProviderImpl(new PropertiesMock());
    final JwtTokenProvider expiredProvider = new JwtTokenProviderImpl(new PropertiesMock(true));

    @Test
    void 토큰_발급_테스트() {
        var at = provider.generateToken("subject", "roles", "claims");
        var rt = provider.generateRefresh("subject");

        assertNotNull(at.orElse(null));
        assertNotNull(rt.orElse(null));
    }

    @Test
    void 유저_객체_생성_테스트() {
        var accessToken = provider.generateToken("subject", "roles", "claims");
        AuthUser userFromToken = provider.getUserFromToken(accessToken.orElse(null));

        assertEquals(userFromToken.getUsername(), "subject");
        assertEquals(userFromToken.getRoles(), "\"roles\"");
        assertEquals(userFromToken.getClaims(), List.of("\"claims\""));
    }

    @Test
    void 토큰_유효성_검사_테스트() {
        var accessToken = provider.generateToken("subject", "roles", "claims");
        DecodedJWT decodedJWT = provider.validateToken(accessToken.orElse(null));
        assertNotNull(decodedJWT);
    }

    @Test
    void 토큰_서명_예외_테스트() {
        var refreshToken = provider.generateRefresh("subject");
        assertThrows(InvalidSignatureException.class, () -> provider.validateToken(refreshToken.orElse(null)));
    }

    @Test
    void 토큰_만료_예외_테스트() {
        var expiredRefreshToken = expiredProvider.generateRefresh("subject");
        assertThrows(ExpiredException.class, () -> expiredProvider.validateToken(expiredRefreshToken.orElse(null)));
    }

    @Test
    void 토큰_형식_예외_테스트() {
        assertThrows(MalformedTokenException.class, () -> expiredProvider.validateToken("malformed token"));
    }

    @Test
    void 만료되지_않은_토큰_테스트() {
        assertFalse(provider
                .isExpired(provider.generateToken("subject", "roles", "claims").orElse(null))
        );
        assertFalse(provider
                .isExpired(provider.generateRefresh("subject").orElse(null))
        );
    }

    @Test
    void 만료된_토큰_테스트() {
        assertFalse(provider
                .isExpired(provider.generateToken("subject", "roles", "claims").orElse(null))
        );
        assertFalse(provider
                .isExpired(provider.generateRefresh("subject").orElse(null))
        );
    }
}