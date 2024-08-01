package com.technet.backend.controller.auth;

import com.technet.backend.model.dto.users.UserAuthDto;
import com.technet.backend.service.auth.AuthService;
import com.technet.backend.service.auth.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.technet.backend.model.dto.auth.*;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class authController {
    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest login) {
        return ResponseEntity.ok(authService.login(login));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody UserAuthDto user){
        return ResponseEntity.ok(authService.register(user));
    }
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validate(@RequestBody AuthResponse request){
        return ResponseEntity.ok(authService.validate(request.token()));
    }
}
