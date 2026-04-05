package com.lttn.quanlytaisan.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (!isValidAuthHeader(authHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(BEARER_PREFIX.length());

        try {
            if (jwtUtil.validateToken(jwt)) {
                String userEmail = jwtUtil.extractUsername(jwt);

                if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    authenticateUser(request, userEmail);
                }
            } else {
                log.debug("JWT token validation failed for request: {}", request.getRequestURI());
            }
        } catch (Exception e) {
            log.warn("JWT processing error for request {}: {}", request.getRequestURI(), e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private boolean isValidAuthHeader(String authHeader) {
        return authHeader != null && authHeader.startsWith(BEARER_PREFIX);
    }

    private void authenticateUser(HttpServletRequest request, String userEmail) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            String token = extractToken(request.getHeader(AUTHORIZATION_HEADER));
            if (jwtUtil.validateToken(token, userDetails.getUsername())) {
                String userId = jwtUtil.extractUserId(token);

                // Credentials must hold the raw JWT so SecurityHelper#getCurrentUserIdOrThrow can read the userId claim.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        token,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);

                log.debug("Successfully authenticated user: {} (ID: {})", maskEmail(userEmail), userId != null ? userId : "N/A");
            }
        } catch (Exception e) {
            log.warn("Failed to load user details for: {}", maskEmail(userEmail));
        }
    }

    private String extractToken(String authHeader) {
        return authHeader != null ? authHeader.substring(BEARER_PREFIX.length()) : null;
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
