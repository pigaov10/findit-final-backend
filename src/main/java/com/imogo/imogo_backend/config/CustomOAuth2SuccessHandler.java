package com.imogo.imogo_backend.config;

import com.imogo.imogo_backend.model.User;
import com.imogo.imogo_backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepo;
    @Autowired private JwtUtil jwtUtil;

    @Value("${app.jwt.redirect}")
    private String redirectUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        User user = userRepo.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setName(name);
                    newUser.setFromSocial(true);
                    newUser.setPassword(UUID.randomUUID().toString());
                    return userRepo.save(newUser);
                });

        String token = jwtUtil.generateToken(user);
        if (user.getRole() == null) {
            response.sendRedirect(redirectUrl + "/choose-role?token=" + token);
        } else {
            response.sendRedirect(redirectUrl + "?token=" + token);
        }
    }
}
