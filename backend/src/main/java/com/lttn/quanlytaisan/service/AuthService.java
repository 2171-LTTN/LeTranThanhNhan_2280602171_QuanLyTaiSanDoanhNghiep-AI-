package com.lttn.quanlytaisan.service;

import com.lttn.quanlytaisan.dto.request.LoginRequest;
import com.lttn.quanlytaisan.dto.request.RegisterRequest;
import com.lttn.quanlytaisan.dto.response.LoginResponse;
import com.lttn.quanlytaisan.dto.response.UserResponse;
import com.lttn.quanlytaisan.exception.BusinessException;
import com.lttn.quanlytaisan.mapper.UserMapper;
import com.lttn.quanlytaisan.model.User;
import com.lttn.quanlytaisan.repository.UserRepository;
import com.lttn.quanlytaisan.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private static final String DEFAULT_ROLE = "USER";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    public UserResponse register(RegisterRequest request) {
        log.info("Registration attempt for email: {}", maskEmail(request.getEmail()));

        validateRegistrationRequest(request);

        if (userRepository.existsByEmail(request.getEmail().toLowerCase())) {
            log.warn("Registration failed - email already exists: {}", maskEmail(request.getEmail()));
            throw new BusinessException("Email already registered");
        }

        User user = buildUserEntity(request);
        User savedUser = userRepository.save(user);

        log.info("User registered successfully: {} (ID: {})", maskEmail(savedUser.getEmail()), savedUser.getId());

        return userMapper.toResponse(savedUser);
    }

    public LoginResponse login(LoginRequest request) {
        log.info("Login attempt for email: {}", maskEmail(request.getEmail()));

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail().toLowerCase(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            log.warn("Login failed - invalid credentials for email: {}", maskEmail(request.getEmail()));
            throw e;
        } catch (Exception e) {
            log.error("Login authentication error for email: {}", maskEmail(request.getEmail()), e);
            throw new BadCredentialsException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.getEmail().toLowerCase())
                .orElseThrow(() -> {
                    log.error("User found during authentication but not in database: {}", maskEmail(request.getEmail()));
                    return new BadCredentialsException("Invalid email or password");
                });

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        log.info("User logged in successfully: {} (ID: {})", maskEmail(user.getEmail()), user.getId());

        return LoginResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .user(userMapper.toResponse(user))
                .build();
    }

    public UserResponse getUserByEmail(String email) {
        log.debug("Fetching user by email: {}", maskEmail(email));

        return userRepository.findByEmail(email.toLowerCase())
                .map(userMapper::toResponse)
                .orElseThrow(() -> {
                    log.error("User not found: {}", maskEmail(email));
                    return new BusinessException("User not found");
                });
    }

    private void validateRegistrationRequest(RegisterRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new BusinessException("Email is required");
        }
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new BusinessException("Password must be at least 6 characters");
        }
    }

    private User buildUserEntity(RegisterRequest request) {
        return User.builder()
                .name(request.getName().trim())
                .email(request.getEmail().toLowerCase().trim())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(DEFAULT_ROLE)
                .createdAt(java.time.LocalDateTime.now())
                .build();
    }

    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "***";
        }
        int atIndex = email.indexOf("@");
        if (atIndex <= 1) {
            return "***" + email.substring(atIndex);
        }
        return email.substring(0, 2) + "***" + email.substring(atIndex);
    }
}
