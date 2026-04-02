package com.lttn.quanlytaisan.controller;

import com.lttn.quanlytaisan.dto.response.ApiResponse;
import com.lttn.quanlytaisan.dto.response.UserResponse;
import com.lttn.quanlytaisan.security.SecurityHelper;
import com.lttn.quanlytaisan.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
@Slf4j
public class UserController {

    private final UserService userService;
    private final SecurityHelper securityHelper;

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
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.name")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable String id) {
        log.debug("GET /api/users/{}", id);

        UserResponse user = userService.getUserById(id);

        return ResponseEntity.ok(ApiResponse.success(user));
    }
}
