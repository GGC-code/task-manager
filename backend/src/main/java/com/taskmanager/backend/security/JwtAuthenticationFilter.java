package com.taskmanager.backend.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component // registra il filtro come componente gestito da Spring
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1) Leggiamo l'intestazione Authorization della richiesta.
        String authHeader = request.getHeader("Authorization");

        // 2) Se manca o non inizia con "Bearer ", non autentichiamo nessuno
        //    e lasciamo proseguire la richiesta così com'è (sarà la SecurityConfig a decidere se bloccarla).
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3) Estraiamo il token togliendo il prefisso "Bearer " (7 caratteri).
        String token = authHeader.substring(7);

        // 4) Se il token è valido e non c'è già un'autenticazione in corso, la impostiamo.
        if (jwtService.isTokenValid(token)
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            Long userId = jwtService.extractUserId(token);

            // Creiamo l'oggetto che rappresenta "l'utente autenticato" per Spring Security.
            // Come principal mettiamo l'id dell'utente; nessuna authority specifica per ora.
            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                    userId, null, Collections.emptyList());

            // Registriamo l'autenticazione nel contesto di sicurezza: da qui in poi,
            // per la durata di questa richiesta, l'utente è considerato autenticato.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 5) In ogni caso la richiesta prosegue verso il filtro successivo (e infine il controller).
        filterChain.doFilter(request, response);
    }
}