package com.security.simple.utils;

import com.security.simple.AutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Import(AutoConfiguration.class)
class PasswordEncoderTest {

    final PasswordEncoder encoder = new PasswordEncoder();

    @Test
    void 암호화된_비밀번호를_설정하고_이를_검증() {
        String password = "PassW0!D&*()123";
        String encoded = encoder.encode(password);

        assertTrue(encoder.matches(password, encoded));
    }
}