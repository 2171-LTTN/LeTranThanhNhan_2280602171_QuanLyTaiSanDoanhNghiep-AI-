package com.lttn.quanlytaisan.controller;

import com.lttn.quanlytaisan.dto.request.CreateUserRequest;
import com.lttn.quanlytaisan.dto.response.ApiResponse;
import com.lttn.quanlytaisan.dto.response.UserResponse;
import com.lttn.quanlytaisan.repository.UserRepository;
import com.lttn.quanlytaisan.security.SecurityHelper;
import com.lttn.quanlytaisan.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final SecurityHelper securityHelper;
    private final UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.debug("GET /api/users - page: {}, size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<UserResponse> users = userService.getAllUsers(pageable);

        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable String id) {
        log.debug("GET /api/users/{}", id);

        String currentEmail = securityHelper.getCurrentUserEmailOrThrow();
        UserResponse currentUser = userService.getUserByEmail(currentEmail);

        if (!currentUser.getId().equals(id) && !"ADMIN".equals(currentUser.getRole())) {
            throw new com.lttn.quanlytaisan.exception.BusinessException("Access denied");
        }

        UserResponse user = userService.getUserById(id);

        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest request
    ) {
        log.info("POST /api/users - Creating user: {}", maskEmail(request.getEmail()));

        String performedBy = securityHelper.getCurrentUserEmailOrDefault("system");
        UserResponse user = userService.createUser(request, performedBy);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(user, "User created successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String id) {
        log.info("DELETE /api/users/{}", id);

        String performedBy = securityHelper.getCurrentUserEmailOrDefault("system");
        userService.deleteUser(id, performedBy);

        return ResponseEntity.ok(ApiResponse.success(null, "User deleted successfully"));
    }

    /**
     * TEMP: Creates the first admin account when no users exist.
     * This endpoint is open (no auth) and self-destructs after use.
     * Access: POST /api/users/init-admin
     * Body: { "email": "...", "password": "...", "name": "..." }
     */
    @PostMapping("/init-admin")
    public ResponseEntity<ApiResponse<UserResponse>> initFirstAdmin(
            @RequestBody CreateUserRequest request
    ) {
        if (userRepository.countByRole("ADMIN") > 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("System already has at least one admin. Login with existing admin to create more."));
        }
        request.setRole("ADMIN");
        String performedBy = "system";
        UserResponse user = userService.createUser(request, performedBy);
        log.info("First admin account created via init-admin: {}", maskEmail(user.getEmail()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(user, "Admin account created successfully"));
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
