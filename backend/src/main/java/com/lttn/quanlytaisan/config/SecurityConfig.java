package com.lttn.quanlytaisan.config;

import com.lttn.quanlytaisan.security.CustomUserDetailsService;
import com.lttn.quanlytaisan.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    @Value("${cors.allowed-origins:http://localhost:5173,http://localhost:3000}")
    private String allowedOrigins;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints - no authentication required
                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ===== ADMIN ONLY ENDPOINTS =====
                        // User management - ADMIN only
                        .requestMatchers("/api/users/init-admin").permitAll()
                        .requestMatchers("/api/users").hasRole("ADMIN")
                        .requestMatchers("/api/users/*").hasRole("ADMIN")
                        .requestMatchers("/api/users/*/*").hasRole("ADMIN")

                        // Asset management - ADMIN only
                        .requestMatchers(HttpMethod.POST, "/api/assets").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/assets/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/assets/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/assets/*/assign").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/assets/*/return").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/assets/*/report-broken").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/assets/*/mark-available").hasRole("ADMIN")

                        // Dashboard stats - ADMIN only
                        .requestMatchers("/api/dashboard/**").hasRole("ADMIN")

                        // User allocation requests (must be before GET /api/requests/* which matches one segment)
                        .requestMatchers(HttpMethod.GET, "/api/requests/my").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/requests/my-stats").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/requests/me/allocation-requests").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/requests/*/cancel").hasAnyRole("USER", "ADMIN")

                        // Request management - ADMIN only
                        .requestMatchers("/api/requests/stats").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/requests").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/requests/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/requests/*/approve").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/requests/*/reject").hasRole("ADMIN")

                        // ===== USER ENDPOINTS =====
                        // Users can view assets (read-only)
                        .requestMatchers(HttpMethod.GET, "/api/assets/**").hasAnyRole("USER", "ADMIN")

                        // Users can view their own profile
                        .requestMatchers("/api/auth/me").authenticated()

                        // All other requests require authentication
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.setContentType("application/json");
                            response.getWriter().write("{\"success\":false,\"message\":\"Access denied. Insufficient permissions.\"}");
                        })
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(parseOrigins());
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization", "Content-Type", "X-Requested-With", "Accept", "Accept-Language"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private List<String> parseOrigins() {
        if (allowedOrigins == null || allowedOrigins.isBlank()) {
            throw new IllegalStateException(
                    "cors.allowed-origins must be set via CORS_ORIGINS environment variable");
        }
        return Arrays.asList(allowedOrigins.split(","));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
