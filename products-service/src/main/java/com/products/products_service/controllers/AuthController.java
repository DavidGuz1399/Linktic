package com.products.products_service.controllers;

import com.products.products_service.config.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;

    @PostMapping("/login")

    public Map<String, String> login(@RequestBody Map<String, String> credentials) {

        String user = credentials.get("username");
        String pass = credentials.get("password");

        if("admin".equals(user) && "pr0duc7S3rv1C3".equals(pass)) {
            return Map.of("token", jwtUtil.generateToken(user));
        }
        throw new RuntimeException("Credenciales invalidas");
    }
}
