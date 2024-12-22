package com.security.simple.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.security.simple.auth.AuthUser;
import com.security.simple.auth.AuthUserImpl;
import com.security.simple.config.Properties;
import com.security.simple.config.SecurityProperties.Jwt.TokenConfig;
import com.security.simple.filter.exception.*;
import com.security.simple.utils.JwtAlgorithmFactory;
import com.security.simple.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProviderImpl implements JwtTokenProvider {

    private final Properties properties;

    @Override
    public String generateToken(String subject, String roles, String claims) {
        TokenConfig access = properties.getJwt().getAccess();
        try {
            return JWT.create()
                    .withSubject(subject)
                    .withExpiresAt(new Date(System.currentTimeMillis() + (access.getExpiringTime().getSeconds() * 1000)))
                    .withClaim("roles", roles)
                    .withClaim("claims", claims)
                    .sign(JwtAlgorithmFactory.createAlgorithm(
                            access.getAlgorithm(),
                            access.getPrivateKey(),
                            access.getPublicKey())
                    );
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String generateRefresh(String subject) {
        TokenConfig refresh = properties.getJwt().getRefresh();
        try {
            return JWT.create()
                    .withSubject(subject)
                    .withExpiresAt(new Date(System.currentTimeMillis() + (refresh.getExpiringTime().getSeconds() * 1000)))
                    .sign(JwtAlgorithmFactory.createAlgorithm(
                            refresh.getAlgorithm(),
                            refresh.getPrivateKey(),
                            refresh.getPublicKey())
                    );
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public AuthUser getUserFromToken(String token) {
        return new AuthUserImpl(validateToken(token));
    }

    @Override
    public DecodedJWT validateToken(String token) {
        TokenConfig access = properties.getJwt().getAccess();
        try {
            return JWT.require(JwtAlgorithmFactory.createAlgorithm(
                            access.getAlgorithm(),
                            access.getPrivateKey(),
                            access.getPublicKey())
                    )
                    .build()
                    .verify(token);
        } catch (TokenExpiredException e) {
            throw new ExpiredException();
        } catch (SignatureVerificationException e) {
            throw new InvalidSignatureException();
        } catch (JWTDecodeException e) {
            throw new MalformedTokenException();
        } catch (IllegalArgumentException e) {
            throw new InvalidTokenInputException();
        } catch (Exception e) {
            throw new UnknownTokenException();
        }
    }

    @Override
    public boolean isExpired(String token) {
        DecodedJWT decode = JWT.decode(token);
        return decode.getExpiresAt().after(new Date());
    }
}
