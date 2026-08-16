package com.taskmanager.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // ATTENZIONE: configurazione TEMPORANEA per lo sviluppo
    // Verrà sostituita dalla sicurezza con JWT nel Capitolo 3
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // disabilita la protezione CSRF
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin())) // abilita l'uso dei frame per H2 Console
            .authorizeHttpRequests(auth -> auth // permette ogni richiesta senza autenticazione
                .anyRequest().permitAll()
            );
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() { //codificatore BCrypt, algoritmo di hashing resistente agli attacchi a forza bruta
    return new BCryptPasswordEncoder();
}
}
