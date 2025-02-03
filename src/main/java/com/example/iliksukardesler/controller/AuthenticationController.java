package com.example.iliksukardesler.controller;

import com.example.iliksukardesler.dto.LoginRequest;
import com.example.iliksukardesler.dto.UserDto;
import com.example.iliksukardesler.model.User;
import com.example.iliksukardesler.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            String result = userService.register(user);
            return ResponseEntity.ok(Collections.singletonMap("message", result));  // JSON formatında mesaj dön
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            String token = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", "Invalid credentials"));
        }
    }
}