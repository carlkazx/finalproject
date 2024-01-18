package com.example.newproject.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            AuthenticationProvider authenticationProvider
    ) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults()) // Enable CORS
                .csrf().disable()
                .authorizeRequests(authorize -> authorize
                        .requestMatchers("/auth/login", "/auth/signup").permitAll() // Public endpoints
                        .requestMatchers(HttpMethod.POST, "/api/overtime/approve/*",
                                "/api/tasks/approve/*",
                                "/api/tasks/assign",
                                "/api/tasks/revert/*").hasRole("ADMIN") // Only ADMIN can access these
                        .requestMatchers(HttpMethod.POST, "/admins").hasRole("SUPER_ADMIN") // Only SUPER_ADMIN can access this
                        .requestMatchers(HttpMethod.GET, "/users").hasAnyRole("ADMIN", "SUPER_ADMIN") // Both ADMIN and SUPER_ADMIN can access this
                        .requestMatchers(HttpMethod.POST, "/api/overtime/overtime-request").hasRole("USER") // Only USER can access this
                        .requestMatchers("/api/**").permitAll() // All other /api endpoints are public
                        .anyRequest().authenticated() // All other requests need to be authenticated
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.applyPermitDefaultValues();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // Specify your frontend URL
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
