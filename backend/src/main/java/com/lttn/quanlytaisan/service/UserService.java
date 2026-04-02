package com.lttn.quanlytaisan.service;

import com.lttn.quanlytaisan.dto.response.UserResponse;
import com.lttn.quanlytaisan.exception.ResourceNotFoundException;
import com.lttn.quanlytaisan.mapper.UserMapper;
import com.lttn.quanlytaisan.model.User;
import com.lttn.quanlytaisan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

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

    public Map<String, Long> getUserStats() {
        log.debug("Fetching user statistics");

        Map<String, Long> stats = new HashMap<>();
        stats.put("total", userRepository.count());
        stats.put("admins", userRepository.countByRole("ADMIN"));
        stats.put("users", userRepository.countByRole("USER"));

        log.debug("User statistics: {}", stats);

        return stats;
    }
}
