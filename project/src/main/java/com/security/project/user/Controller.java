package com.security.project.user;

import com.security.project.user.Request.RegisterRequest;
import com.security.project.user.Response.LoginResponse;
import com.security.simple.utils.JwtTokenProvider;
import com.security.simple.utils.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

import static com.security.project.user.Request.LoginRequest;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final JwtTokenProvider provider;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

    /**
     * @brief
     * 1. Verify user
     * 2. Generate tokens
     * 3. Response
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        if (!verityUser(request.username(), request.password())){
            return ResponseEntity.badRequest().body("Invalid login info");
        }

        var accessToken = provider.generateToken(request.username(), "user", null)
                .orElseThrow(RuntimeException::new);
        var refreshToken = provider.generateRefresh(request.username())
                .orElseThrow(RuntimeException::new);

        userRepository.updateRefreshByUser(request.username(), refreshToken);

        return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken));
    }

    @PostMapping("/register")
    public ResponseEntity<?> signup(@RequestBody RegisterRequest request) throws URISyntaxException {

        if (userRepository.existByUsername(request.username()))
            throw new RuntimeException("Username is already in use");

        userRepository.save(new User(request.username(), encoder.encode(request.password()), null));

        return ResponseEntity.created(new URI("/users/" + request.username())).build();
    }

    /**
     * @brief
     * 1. Check exist
     * 2. Check if it has expired
     * 3. Return after renewal
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody String refreshToken){

        var currentUser = userRepository.findUserByToken(refreshToken);

        if (currentUser.isEmpty()) {
            return ResponseEntity.badRequest().body("Refresh token does not exist");
        }

        var username = currentUser.get().getUsername();
        if (!currentUser.get().getRefreshToken().equals(refreshToken)){
            userRepository.updateRefreshByUser(username, null);
            return ResponseEntity.badRequest().body("Refresh token does not match");
        }

        if (provider.isExpired(refreshToken)){
            userRepository.updateRefreshByUser(username, null);
            return ResponseEntity.badRequest().body("Refresh token expired");
        }

        userRepository.updateRefreshByUser(username, refreshToken);

        var token = provider.generateToken(username, "user", null)
                .orElseThrow(RuntimeException::new);

        return ResponseEntity.ok(new LoginResponse(token, null));
    }

    @GetMapping("/test-url")
    public ResponseEntity<?> testUrl(){
        return ResponseEntity.ok("Authorization successful");
    }

    /**
     * @brief
     * 1. Find a user by username
     * 2. Make sure your passwords match
     */
    private boolean verityUser(String username, String password) {

        var currentUser = userRepository.findByUsername(username).orElseThrow(RuntimeException::new);
        var originPassword = currentUser.getPassword();

        return encoder.matches(password, originPassword);
    }
}
