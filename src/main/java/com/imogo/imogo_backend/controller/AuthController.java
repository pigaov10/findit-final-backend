package com.imogo.imogo_backend.controller;

import com.imogo.imogo_backend.config.JwtUtil;
import com.imogo.imogo_backend.dto.LoginRequest;
import com.imogo.imogo_backend.dto.RegisterRequest;
import com.imogo.imogo_backend.model.User;
import com.imogo.imogo_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody RegisterRequest request) {
        if (userRepo.findByEmail(request.email).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.email);
        user.setPassword(passwordEncoder.encode(request.password));
        user.setName(request.name);
        user.setRole(request.role);
        user.setFromSocial(false);
        userRepo.save(user);

        String token = jwtUtil.generateToken(user);
        return Map.of("token", token);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {
        User user = userRepo.findByEmail(request.email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user);
        return Map.of("token", token);
    }
}
