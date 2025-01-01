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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProviderImpl implements JwtTokenProvider {

    private final Properties props;

    @Override
    public Optional<String> generateToken(String subject, String roles, String claims) {
        TokenConfig access = props.getJwt().getAccess();
        try {
            return Optional.of(JWT.create()
                    .withSubject(subject)
                    .withExpiresAt(new Date(System.currentTimeMillis() + (access.getExpiringTime().getSeconds() * 1000)))
                    .withClaim("roles", roles)
                    .withClaim("claims", claims)
                    .sign(JwtAlgorithmFactory.createAlgorithm(
                            access.getAlgorithm(),
                            access.getPrivateKey(),
                            access.getPublicKey())
                    ));
        } catch (Exception e) {
            log.debug("JwtTokenProvider : Error occurred in generating access token, {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> generateRefresh(String subject) {
        TokenConfig refresh = props.getJwt().getRefresh();
        try {
            return Optional.of(JWT.create()
                    .withSubject(subject)
                    .withExpiresAt(new Date(System.currentTimeMillis() + (refresh.getExpiringTime().getSeconds() * 1000)))
                    .sign(JwtAlgorithmFactory.createAlgorithm(
                            refresh.getAlgorithm(),
                            refresh.getPrivateKey(),
                            refresh.getPublicKey())
                    ));
        } catch (Exception e) {
            log.debug("JwtTokenProvider : Error occurred in generating refresh token, {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public AuthUser getUserFromToken(String token) {
        return new AuthUserImpl(validateToken(token));
    }

    @Override
    public DecodedJWT validateToken(String token) {
        TokenConfig access = props.getJwt().getAccess();
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
        return decode.getExpiresAt().before(new Date());
    }
}
