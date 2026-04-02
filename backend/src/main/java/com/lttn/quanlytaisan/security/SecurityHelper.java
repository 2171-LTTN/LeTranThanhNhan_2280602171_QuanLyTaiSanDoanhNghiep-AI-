package com.lttn.quanlytaisan.security;

import com.lttn.quanlytaisan.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Helper component for extracting current user information from SecurityContext.
 * Centralizes security context access logic.
 */
@Component
@Slf4j
public class SecurityHelper {

    /**
     * Extracts the current authenticated user's email from SecurityContext.
     *
     * @return the email of the current user
     * @throws BusinessException if no authenticated user is found
     */
    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
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

    /**
     * Extracts the current authenticated user's email, or returns default value if not authenticated.
     *
     * @param defaultValue the default value to return if not authenticated
     * @return the email of the current user or defaultValue
     */
    public String getCurrentUserEmailOrDefault(String defaultValue) {
        try {
            return getCurrentUserEmail();
        } catch (BusinessException e) {
            return defaultValue;
        }
    }

    /**
     * Checks if the current user has ADMIN role.
     *
     * @return true if current user has ADMIN role
     */
    public boolean isCurrentUserAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return false;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }
}
