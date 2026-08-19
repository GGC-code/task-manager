package com.taskmanager.backend.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    // Valori letti da application.properties tramite @Value.
    private final SecretKey signingKey;
    private final long expirationMillis;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration}") long expirationMillis) {
        // La stringa segreta viene trasformata in una chiave crittografica per la firma.
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMillis = expirationMillis;
    }

    // Genera un token per un utente: mette il suo id come "subject" e lo username come dato aggiuntivo.
    public String generateToken(Long userId, String username) {
        Date now = new Date();
        Date scadenza = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
            .subject(String.valueOf(userId)) // il "soggetto" del token è l'id dell'utente
            .claim("username", username)     // un'informazione extra trasportata nel token
            .issuedAt(now)                   // quando è stato emesso
            .expiration(scadenza)            // quando scade
            .signWith(signingKey)            // firma con la chiave segreta: rende il token non falsificabile
            .compact();                      // produce la stringa finale del token
    }

    // Legge e verifica un token, restituendo l'id dell'utente contenuto.
    // Se il token è manomesso o scaduto, la libreria solleva un'eccezione.
    public Long extractUserId(String token) {
        Claims claims = parseClaims(token);
        return Long.valueOf(claims.getSubject());
    }

    public String extractUsername(String token) {
        return parseClaims(token).get("username", String.class);
    }

    // Verifica che il token sia valido (firma corretta e non scaduto).
    public boolean isTokenValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false; // firma non valida, token scaduto o malformato
        }
    }

    // Metodo interno: verifica la firma con la chiave segreta ed estrae il contenuto del token.
    private Claims parseClaims(String token) {
        return Jwts.parser()
            .verifyWith(signingKey)   // controlla che la firma corrisponda alla nostra chiave
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}