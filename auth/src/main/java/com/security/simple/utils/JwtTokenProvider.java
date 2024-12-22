package com.security.simple.utils;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.security.simple.auth.AuthUser;

public interface JwtTokenProvider {

    /**
     * @return Created token
     * @brief Create a jwt access token with the relevant information.
     */
    String generateToken(String subject, String roles, String claims);

    /**
     * @return Created token
     * @brief Create a jwt refresh token with the relevant information.
     */
    String generateRefresh(String subject);

    /**
     * @return Result of decrypting the token
     * @brief Verify that the token is valid
     */
    DecodedJWT validateToken(String token);

    /**
     * @return Information of authentication
     * @brief Retrieve user information resulting from decrypting the token
     */
    AuthUser getUserFromToken(String token);

    /**
     * @return Whether the token has expired
     * @brief Used to verify token expiration
     */
    boolean isExpired(String token);
    }
