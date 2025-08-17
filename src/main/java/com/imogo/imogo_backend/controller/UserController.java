package com.imogo.imogo_backend.controller;

import com.imogo.imogo_backend.config.JwtUtil;
import com.imogo.imogo_backend.dto.RoleRequest;
import com.imogo.imogo_backend.model.User;
import com.imogo.imogo_backend.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/set-role")
    public ResponseEntity<?> setRole(
            @CookieValue("jwt") String tempToken,
            HttpServletResponse response,
            @RequestBody RoleRequest roleRequest) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // username = email no seu caso

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        user.setRole(roleRequest.getRole());
        userRepository.save(user);
        String finalToken = jwtUtil.generateTokenWithRole(user, roleRequest.getRole());
        Cookie jwtCookie = new Cookie("jwt", finalToken);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(60 * 60); // token final válido 1h
        response.addCookie(jwtCookie);

        return ResponseEntity.ok("Role atualizada com sucesso");
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        UserResponse response = new UserResponse(
                user.getEmail(),
                user.getRole(),
                user.getName()
        );

        return ResponseEntity.ok(response);
    }
    public record UserResponse(String email, String role, String name) {}
}
