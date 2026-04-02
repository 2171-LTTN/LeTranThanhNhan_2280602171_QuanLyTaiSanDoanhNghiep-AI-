package com.lttn.quanlytaisan.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", "YourSuperSecretKeyForJWTTokenGenerationThatIsAtLeast256BitsLong!!");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 86400000L);
        jwtUtil.init();
    }

    @Test
    void generateToken_Success() {
        String token = jwtUtil.generateToken("test@example.com");

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void generateToken_WithRole_Success() {
        String token = jwtUtil.generateToken("test@example.com", "ADMIN");

        assertNotNull(token);
        assertNotNull(jwtUtil.extractRole(token));
    }

    @Test
    void extractUsername_Success() {
        String token = jwtUtil.generateToken("test@example.com");

        String username = jwtUtil.extractUsername(token);

        assertEquals("test@example.com", username);
    }

    @Test
    void validateToken_ValidToken_ReturnsTrue() {
        String token = jwtUtil.generateToken("test@example.com");

        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void validateToken_InvalidToken_ReturnsFalse() {
        assertFalse(jwtUtil.validateToken("invalid.token.here"));
    }

    @Test
    void validateToken_WithUsername_Success() {
        String token = jwtUtil.generateToken("test@example.com");

        assertTrue(jwtUtil.validateToken(token, "test@example.com"));
        assertFalse(jwtUtil.validateToken(token, "wrong@example.com"));
    }

    @Test
    void extractRole_Success() {
        String token = jwtUtil.generateToken("test@example.com", "ADMIN");

        String role = jwtUtil.extractRole(token);

        assertEquals("ADMIN", role);
    }

    @Test
    void extractExpiration_Success() {
        String token = jwtUtil.generateToken("test@example.com");

        var expiration = jwtUtil.extractExpiration(token);

        assertNotNull(expiration);
        assertTrue(expiration.after(new java.util.Date()));
    }
}
