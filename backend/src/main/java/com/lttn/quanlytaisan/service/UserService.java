package com.lttn.quanlytaisan.service;

import com.lttn.quanlytaisan.dto.request.CreateUserRequest;
import com.lttn.quanlytaisan.dto.response.UserResponse;
import com.lttn.quanlytaisan.exception.BusinessException;
import com.lttn.quanlytaisan.exception.ResourceNotFoundException;
import com.lttn.quanlytaisan.mapper.UserMapper;
import com.lttn.quanlytaisan.model.User;
import com.lttn.quanlytaisan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private static final Set<String> VALID_ROLES = Set.of("USER", "ADMIN");

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public Page<UserResponse> getAllUsers(Pageable pageable) {
        log.debug("Fetching all users, page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        return userRepository.findAll(pageable).map(userMapper::toResponse);
    }

    public UserResponse getUserById(String id) {
        log.debug("Fetching user by ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found: {}", id);
                    return new ResourceNotFoundException("User not found: " + id);
                });
        return userMapper.toResponse(user);
    }

    public UserResponse getUserByEmail(String email) {
        log.debug("Fetching user by email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("User not found with email: {}", email);
                    return new ResourceNotFoundException("User not found: " + email);
                });
        return userMapper.toResponse(user);
    }

    public UserResponse createUser(CreateUserRequest request, String performedBy) {
        log.info("Creating user: {} by: {}", maskEmail(request.getEmail()), performedBy);

        if (userRepository.existsByEmail(request.getEmail().toLowerCase())) {
            log.warn("User creation failed - email already exists: {}", maskEmail(request.getEmail()));
            throw new BusinessException("Email already registered");
        }

        if (!isValidRole(request.getRole())) {
            log.warn("User creation failed - invalid role: {}", request.getRole());
            throw new BusinessException("Invalid role. Must be USER or ADMIN");
        }

        User user = User.builder()
                .name(request.getName().trim())
                .email(request.getEmail().toLowerCase().trim())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .isActive(true)
                .build();

        User saved = userRepository.save(user);

        log.info("User created successfully: {} (ID: {})", maskEmail(saved.getEmail()), saved.getId());

        return userMapper.toResponse(saved);
    }

    public void deleteUser(String id, String performedBy) {
        log.info("Deleting user: {} by: {}", id, performedBy);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found: {}", id);
                    return new ResourceNotFoundException("User not found: " + id);
                });

        if ("ADMIN".equals(user.getRole())) {
            throw new BusinessException("Admin accounts cannot be deleted");
        }

        userRepository.delete(user);

        log.info("User deleted successfully: {} (ID: {})", maskEmail(user.getEmail()), user.getId());
    }

    public Map<String, Long> getUserStats() {
        log.debug("Fetching user statistics");

        Map<String, Long> stats = new HashMap<>();
        stats.put("total", userRepository.count());
        stats.put("admins", userRepository.countByRole("ADMIN"));
        stats.put("users", userRepository.countByRole("USER"));

        log.debug("User statistics: {}", stats);

        return stats;
    }

    public boolean isValidRole(String role) {
        return VALID_ROLES.contains(role);
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
