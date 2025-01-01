package com.security.simple.utils;

import com.auth0.jwt.algorithms.Algorithm;
import com.security.simple.utils.algorithm.JwtAlgorithm;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class JwtAlgorithmFactory {

    public static Algorithm createAlgorithm(JwtAlgorithm algorithm, String secretOrPrivateKey, String publicKey) throws Exception {
        return switch (algorithm) {
            case HS256 -> Algorithm.HMAC256(secretOrPrivateKey);
            case HS384 -> Algorithm.HMAC384(secretOrPrivateKey);
            case HS512 -> Algorithm.HMAC512(secretOrPrivateKey);
            case RS256, PS256 -> Algorithm.RSA256(getPublicKey(publicKey), getPrivateKey(secretOrPrivateKey));
            case RS384, PS384 -> Algorithm.RSA384(getPublicKey(publicKey), getPrivateKey(secretOrPrivateKey));
            case RS512, PS512 -> Algorithm.RSA512(getPublicKey(publicKey), getPrivateKey(secretOrPrivateKey));
        };
    }

    private static RSAPublicKey getPublicKey(String key) throws Exception {
        String publicKeyContent = getKeyContent(key);
        byte[] encoded = Base64.getDecoder().decode(publicKeyContent);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);

        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    private static RSAPrivateKey getPrivateKey(String key) throws Exception {
        String privateKeyContent = getKeyContent(key);
        byte[] encoded = Base64.getDecoder().decode(privateKeyContent);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);

        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    private static String getKeyContent(String key) {
        return key.replaceAll("\\n", "")
                .replaceAll("\\r", "")
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
    }
}
