package com.security.project;

import com.security.project.mock.PropertiesMock;
import com.security.project.user.Controller;
import com.security.project.user.Request.LoginRequest;
import com.security.project.user.Request.RegisterRequest;
import com.security.project.user.Response.LoginResponse;
import com.security.project.user.UserRepository;
import com.security.simple.utils.JwtTokenProviderImpl;
import com.security.simple.utils.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ProjectApplicationTests {

    private final Controller controller = new Controller(
            new JwtTokenProviderImpl(new PropertiesMock()),
            new PasswordEncoder(),
            new UserRepository()
    );

    String username = "username";
    String password = "password";
    String accessToken = null;
    String refreshToken = null;

    @BeforeEach
    void 사용자는_시스템에_회원가입한다() {
        ResponseEntity<?> result = controller.signup(new RegisterRequest(username, password));
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertEquals(Objects.requireNonNull(result.getBody()).toString(), "/users/"+username);
    }

    @Test
    void 사용자는_시스템에_로그인한다() {
        ResponseEntity<?> result = controller.login(new LoginRequest(username, password));
        accessToken = ((LoginResponse) Objects.requireNonNull(result.getBody())).access();
        refreshToken = ((LoginResponse) Objects.requireNonNull(result.getBody())).refresh();
        assertTrue(result.getStatusCode().is2xxSuccessful());
    }

    @Test
    void 사용자는_액세스토큰을_재발급한다() {
        ResponseEntity<?> result = controller.refresh(refreshToken);
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertTrue(true);
    }

    @Test
    void 사용자는_인증이_필요한_API에_접근한다() {
        controller.testUrl();
        assertTrue(true);
    }
}
