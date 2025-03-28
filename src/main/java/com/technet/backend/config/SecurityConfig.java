package com.technet.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        DefaultSecurityFilterChain build = http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(
                        authRequest ->
                                authRequest
                                        .requestMatchers("/inventory/producto/paged").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/inventory/producto/CategoriaProducto").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/inventory/producto/{id}").permitAll()
                                        .requestMatchers(HttpMethod.GET,"/inventory/archivos/publicitaria").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/inventory/marca").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/inventory/categoria").permitAll()
                                        .requestMatchers("/payment/**").permitAll()
                                        .requestMatchers("/auth/**").permitAll()
                                        .anyRequest().authenticated()
                )
                .sessionManagement(sessionManager ->
                        sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
        return build;
    }
}
