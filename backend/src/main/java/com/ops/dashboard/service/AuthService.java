package com.ops.dashboard.service;

import com.ops.dashboard.dto.AuthResponse;
import com.ops.dashboard.dto.LoginRequest;
import com.ops.dashboard.dto.RegisterRequest;
import com.ops.dashboard.model.Role;
import com.ops.dashboard.model.User;
import com.ops.dashboard.repository.UserRepository;
import com.ops.dashboard.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtService jwtService;

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (req.getEmail() != null && !req.getEmail().isBlank()
                && userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = User.builder()
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .fullName(req.getFullName())
                .email(req.getEmail())
                .role(req.getRole() == null ? Role.EMPLOYEE : req.getRole())
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());
        return new AuthResponse(token, user.getUsername(), user.getFullName(), user.getRole().name());
    }

    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());
        return new AuthResponse(token, user.getUsername(), user.getFullName(), user.getRole().name());
    }
}