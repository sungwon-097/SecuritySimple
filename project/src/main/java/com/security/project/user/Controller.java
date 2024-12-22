package com.security.project.user;

import com.security.simple.utils.JwtTokenProvider;
import com.security.simple.utils.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.security.project.user.Request.LoginRequest;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final JwtTokenProvider provider;
    private final PasswordEncoder encoder;

    static Map<String, String> refreshInfo = new HashMap<>();

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

        var accessToken = provider.generateToken(request.username(), "user", null);
        var refreshToken = provider.generateRefresh(request.username());
        refreshInfo.put(refreshToken, request.username());

        return ResponseEntity.ok(new Response.LoginResponse(accessToken, refreshToken));
    }

    /**
     * @brief
     * 1. Check exist
     * 2. Check if it has expired
     * 3. Return after renewal
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody String refreshToken){
        var currentUser = refreshInfo.get(refreshToken);

        if (currentUser == null ) {
            return ResponseEntity.badRequest().body("Refresh token does not exist");
        }
        if (provider.isExpired(refreshToken)){
            refreshInfo.remove(refreshToken);
            return ResponseEntity.badRequest().body("Refresh token expired");
        }
        refreshInfo.put(refreshToken, currentUser);

        return ResponseEntity.ok("Refresh completed");
    }

    /**
     * @brief
     * 1. Find a user by username
     * 2. Make sure your passwords match
     */
    private boolean verityUser(String username, String password) {
        encoder.encode(password);
        return true;
    }
}
