package com.diary.app.service;

import com.diary.app.dto.AuthDto.*;
import com.diary.app.entity.User;
import com.diary.app.repository.UserRepository;
import com.diary.app.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Handles signup/login logic.
 * UserDetailsService is in UserDetailsServiceImpl to avoid a circular
 * dependency with SecurityConfig -> AuthenticationManager -> AuthService -> AuthenticationManager.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already registered");
        }
        User user = User.builder()
            .name(request.name())
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .build();
        userRepository.save(user);
        String token = jwtUtils.generateToken(user);
        return new AuthResponse(token, user.getName(), user.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new RuntimeException("User not found"));
        String token = jwtUtils.generateToken(user);
        return new AuthResponse(token, user.getName(), user.getEmail());
    }
}
