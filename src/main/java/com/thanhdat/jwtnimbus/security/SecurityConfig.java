package com.thanhdat.jwtnimbus.security;

import com.thanhdat.jwtnimbus.filters.JwtAuthenticationFilter;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter filter;
    private final AuthenticationProvider provider;
    public SecurityConfig(JwtAuthenticationFilter filter, AuthenticationProvider provider) {
        this.filter = filter; this.provider = provider;
    }
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(a -> a
                .requestMatchers("/", "/login", "/profile", "/css/**", "/js/**", "/api/auth/**", "/error").permitAll()
                .requestMatchers("/api/users").hasRole("ADMIN")
                .anyRequest().authenticated())
            .authenticationProvider(provider)
            .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(e -> e
                .authenticationEntryPoint((req,res,ex) -> {
                    res.setStatus(401); res.setCharacterEncoding("UTF-8"); res.setContentType("application/json");
                    res.getWriter().write("{\"status\":401,\"message\":\"Bạn cần đăng nhập\"}");
                })
                .accessDeniedHandler((req,res,ex) -> {
                    res.setStatus(403); res.setCharacterEncoding("UTF-8"); res.setContentType("application/json");
                    res.getWriter().write("{\"status\":403,\"message\":\"Bạn không có quyền truy cập\"}");
                }));
        return http.build();
    }
}
