package com.security.simple.utils;

import com.security.simple.utils.algorithm.JwtAlgorithm;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtAlgorithmFactoryTest {

    @Test
    void HS256_알고리즘_생성() throws Exception {
        var algorithm = JwtAlgorithmFactory.createAlgorithm(JwtAlgorithm.HS256,
                "private key",
                "public key");
        assertEquals(algorithm.getName(), "HS256");
    }

    @Test
    void HS384_알고리즘_생성() throws Exception {
        var algorithm = JwtAlgorithmFactory.createAlgorithm(JwtAlgorithm.HS384,
                "private key",
                "public key");
        assertEquals(algorithm.getName(), "HS384");
    }

    @Test
    void HS512_알고리즘_생성() throws Exception {
        var algorithm = JwtAlgorithmFactory.createAlgorithm(JwtAlgorithm.HS512,
                "private key",
                "public key");
        assertEquals(algorithm.getName(), "HS512");
    }

    @Test
    void RS256_알고리즘_생성() throws Exception {
        var rsaKeys = getRsaKeys();
        var rsAlgorithm = JwtAlgorithmFactory.createAlgorithm(JwtAlgorithm.RS256,
                Base64.getEncoder().encodeToString(rsaKeys.getPrivate().getEncoded()),
                Base64.getEncoder().encodeToString(rsaKeys.getPublic().getEncoded()));
        assertEquals(rsAlgorithm.getName(), "RS256");
    }

    @Test
    void RS384_알고리즘_생성() throws Exception {
        var rsaKeys = getRsaKeys();
        var algorithm = JwtAlgorithmFactory.createAlgorithm(JwtAlgorithm.RS384,
                Base64.getEncoder().encodeToString(rsaKeys.getPrivate().getEncoded()),
                Base64.getEncoder().encodeToString(rsaKeys.getPublic().getEncoded()));
        assertEquals(algorithm.getName(), "RS384");
    }

    @Test
    void RS512_알고리즘_생성() throws Exception {
        var rsaKeys = getRsaKeys();
        var algorithm = JwtAlgorithmFactory.createAlgorithm(JwtAlgorithm.RS512,
                Base64.getEncoder().encodeToString(rsaKeys.getPrivate().getEncoded()),
                Base64.getEncoder().encodeToString(rsaKeys.getPublic().getEncoded()));
        assertEquals(algorithm.getName(), "RS512");
    }

    private KeyPair getRsaKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);

        return keyPairGenerator.generateKeyPair();
    }
}