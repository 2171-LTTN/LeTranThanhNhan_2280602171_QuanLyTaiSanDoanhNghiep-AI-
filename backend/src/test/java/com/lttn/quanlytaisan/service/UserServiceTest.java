package com.lttn.quanlytaisan.service;

import com.lttn.quanlytaisan.dto.request.CreateUserRequest;
import com.lttn.quanlytaisan.dto.response.UserResponse;
import com.lttn.quanlytaisan.exception.BusinessException;
import com.lttn.quanlytaisan.exception.ResourceNotFoundException;
import com.lttn.quanlytaisan.mapper.UserMapper;
import com.lttn.quanlytaisan.model.User;
import com.lttn.quanlytaisan.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private User adminUser;
    private UserResponse userResponse;
    private CreateUserRequest createUserRequest;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id("user-123")
                .name("Test User")
                .email("test@example.com")
                .password("encoded_password")
                .role("USER")
                .isActive(true)
                .build();

        adminUser = User.builder()
                .id("admin-001")
                .name("Admin User")
                .email("admin@example.com")
                .password("encoded_password")
                .role("ADMIN")
                .isActive(true)
                .build();

        userResponse = UserResponse.builder()
                .id("user-123")
                .name("Test User")
                .email("test@example.com")
                .role("USER")
                .isActive(true)
                .build();

        createUserRequest = CreateUserRequest.builder()
                .name("Test User")
                .email("test@example.com")
                .password("password123")
                .role("USER")
                .build();
    }

    @Nested
    @DisplayName("Get User Tests")
    class GetUserTests {

        @Test
        @DisplayName("Should get user by ID successfully")
        void shouldGetUserByIdSuccessfully() {
            when(userRepository.findById("user-123")).thenReturn(Optional.of(testUser));
            when(userMapper.toResponse(testUser)).thenReturn(userResponse);

            UserResponse response = userService.getUserById("user-123");

            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo("user-123");
            assertThat(response.getEmail()).isEqualTo("test@example.com");
            verify(userRepository).findById("user-123");
        }

        @Test
        @DisplayName("Should throw exception when user not found by ID")
        void shouldThrowExceptionWhenUserNotFoundById() {
            when(userRepository.findById("nonexistent")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.getUserById("nonexistent"))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("User not found");
        }

        @Test
        @DisplayName("Should get user by email successfully")
        void shouldGetUserByEmailSuccessfully() {
            when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
            when(userMapper.toResponse(testUser)).thenReturn(userResponse);

            UserResponse response = userService.getUserByEmail("test@example.com");

            assertThat(response).isNotNull();
            assertThat(response.getEmail()).isEqualTo("test@example.com");
            verify(userRepository).findByEmail("test@example.com");
        }

        @Test
        @DisplayName("Should throw exception when user not found by email")
        void shouldThrowExceptionWhenUserNotFoundByEmail() {
            when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.getUserByEmail("notfound@example.com"))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("User not found");
        }

        @Test
        @DisplayName("Should get all users with pagination")
        void shouldGetAllUsersWithPagination() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<User> userPage = new PageImpl<>(List.of(testUser));

            when(userRepository.findAll(pageable)).thenReturn(userPage);
            when(userMapper.toResponse(testUser)).thenReturn(userResponse);

            Page<UserResponse> response = userService.getAllUsers(pageable);

            assertThat(response).isNotNull();
            assertThat(response.getTotalElements()).isEqualTo(1);
            assertThat(response.getContent().get(0).getEmail()).isEqualTo("test@example.com");
        }
    }

    @Nested
    @DisplayName("Create User Tests")
    class CreateUserTests {

        @Test
        @DisplayName("Should create user successfully")
        void shouldCreateUserSuccessfully() {
            when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
            when(passwordEncoder.encode("password123")).thenReturn("encoded_password");
            when(userRepository.save(any(User.class))).thenReturn(testUser);
            when(userMapper.toResponse(testUser)).thenReturn(userResponse);

            UserResponse response = userService.createUser(createUserRequest, "admin@example.com");

            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo("user-123");
            assertThat(response.getEmail()).isEqualTo("test@example.com");

            verify(userRepository).existsByEmail("test@example.com");
            verify(passwordEncoder).encode("password123");
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw exception when email already exists")
        void shouldThrowExceptionWhenEmailExists() {
            when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

            assertThatThrownBy(() -> userService.createUser(createUserRequest, "admin@example.com"))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Email already registered");

            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when role is invalid")
        void shouldThrowExceptionWhenRoleIsInvalid() {
            CreateUserRequest invalidRequest = CreateUserRequest.builder()
                    .name("Test User")
                    .email("test@example.com")
                    .password("password123")
                    .role("SUPERADMIN")
                    .build();

            assertThatThrownBy(() -> userService.createUser(invalidRequest, "admin@example.com"))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Invalid role");
        }

        @Test
        @DisplayName("Should validate role correctly")
        void shouldValidateRoleCorrectly() {
            assertThat(userService.isValidRole("USER")).isTrue();
            assertThat(userService.isValidRole("ADMIN")).isTrue();
            assertThat(userService.isValidRole("SUPERADMIN")).isFalse();
            assertThat(userService.isValidRole("GUEST")).isFalse();
            assertThat(userService.isValidRole(null)).isFalse();
        }
    }

    @Nested
    @DisplayName("Delete User Tests")
    class DeleteUserTests {

        @Test
        @DisplayName("Should delete user successfully")
        void shouldDeleteUserSuccessfully() {
            when(userRepository.findById("user-123")).thenReturn(Optional.of(testUser));
            doNothing().when(userRepository).delete(testUser);

            userService.deleteUser("user-123", "admin@example.com");

            verify(userRepository).delete(testUser);
        }

        @Test
        @DisplayName("Should throw exception when trying to delete admin")
        void shouldThrowExceptionWhenDeletingAdmin() {
            when(userRepository.findById("admin-001")).thenReturn(Optional.of(adminUser));

            assertThatThrownBy(() -> userService.deleteUser("admin-001", "admin@example.com"))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Admin accounts cannot be deleted");

            verify(userRepository, never()).delete(any());
        }

        @Test
        @DisplayName("Should throw exception when user not found for deletion")
        void shouldThrowExceptionWhenUserNotFoundForDeletion() {
            when(userRepository.findById("nonexistent")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.deleteUser("nonexistent", "admin@example.com"))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("User not found");
        }
    }

    @Nested
    @DisplayName("User Statistics Tests")
    class UserStatsTests {

        @Test
        @DisplayName("Should get user statistics")
        void shouldGetUserStatistics() {
            when(userRepository.count()).thenReturn(10L);
            when(userRepository.countByRole("ADMIN")).thenReturn(2L);
            when(userRepository.countByRole("USER")).thenReturn(8L);

            var stats = userService.getUserStats();

            assertThat(stats).isNotNull();
            assertThat(stats.get("total")).isEqualTo(10L);
            assertThat(stats.get("admins")).isEqualTo(2L);
            assertThat(stats.get("users")).isEqualTo(8L);
        }
    }
}
