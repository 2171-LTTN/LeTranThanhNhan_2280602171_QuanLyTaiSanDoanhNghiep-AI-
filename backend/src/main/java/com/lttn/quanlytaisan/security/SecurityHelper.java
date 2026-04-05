package com.lttn.quanlytaisan.security;

import com.lttn.quanlytaisan.exception.BusinessException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityHelper {

    private final JwtUtil jwtUtil;

    /**
     * Extracts the current authenticated user's email from SecurityContext.
     */
    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            log.warn("Attempted to get current user but no authentication found");
            throw new BusinessException("No authenticated user found");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            return (String) principal;
        }

        log.error("Unknown principal type: {}", principal.getClass().getName());
        throw new BusinessException("Unable to determine current user");
    }

    public String getCurrentUserEmailOrDefault(String defaultValue) {
        try {
            return getCurrentUserEmail();
        } catch (BusinessException e) {
            return defaultValue;
        }
    }

    public String getCurrentUserEmailOrThrow() {
        return getCurrentUserEmail();
    }

    /**
     * Gets current user ID from JWT token claims.
     */
    public String getCurrentUserIdOrThrow() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException("No authenticated user found");
        }

        String userId = jwtUtil.extractUserIdFromAuthentication(authentication);
        if (userId != null) {
            return userId;
        }

        String email = getCurrentUserEmail();
        return email;
    }

    public boolean isCurrentUserAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return false;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }
}
