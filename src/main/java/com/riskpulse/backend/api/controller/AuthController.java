package com.riskpulse.backend.api.controller;

import com.riskpulse.backend.api.dto.LoginRequest;
import com.riskpulse.backend.api.dto.LoginResponse;
import com.riskpulse.backend.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Basit giriş: DB'de display_name ile eşleşen kullanıcı aranır; bulunursa userId + displayName döner.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        if (request == null || request.displayName() == null || request.displayName().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return userRepository.findFirstByDisplayNameIgnoreCase(request.displayName().trim())
                .map(u -> ResponseEntity.ok(new LoginResponse(u.getUserId(), u.getDisplayName())))
                .orElse(ResponseEntity.status(401).build());
    }
}
