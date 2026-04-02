package com.lttn.quanlytaisan.mapper;

import com.lttn.quanlytaisan.dto.response.UserResponse;
import com.lttn.quanlytaisan.model.User;
import org.springframework.stereotype.Component;

/**
 * Shared mapper utility to eliminate duplicated mapToResponse methods.
 * Centralizes all entity-to-DTO conversions.
 */
@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
