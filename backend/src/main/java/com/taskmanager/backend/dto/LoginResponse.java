package com.taskmanager.backend.dto;

// Cosa restituiamo al login riuscito: il token e qualche dato utile dell'utente.
public record LoginResponse(
    String token,
    Long userId,
    String username
) {
}