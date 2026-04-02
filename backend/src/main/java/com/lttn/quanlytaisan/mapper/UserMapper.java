package com.lttn.quanlytaisan.mapper;

import com.lttn.quanlytaisan.dto.response.UserResponse;
import com.lttn.quanlytaisan.model.User;
import org.springframework.stereotype.Component;

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
                .department(user.getDepartment())
                .position(user.getPosition())
                .phone(user.getPhone())
                .build();
    }
}
