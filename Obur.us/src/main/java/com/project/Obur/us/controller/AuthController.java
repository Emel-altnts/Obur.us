package com.project.Obur.us.controller;

import com.project.Obur.us.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username) {
        String token = jwtService.generateToken(username);
        return ResponseEntity.ok(Map.of("token", token));
    }
}
