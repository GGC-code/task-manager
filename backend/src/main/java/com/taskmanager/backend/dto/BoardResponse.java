package com.taskmanager.backend.dto;

import java.time.Instant;

// Non espone l'intero oggetto User (e quindi mai la password): appiattisce la
// relazione riportando solo id e username del proprietario.
public record BoardResponse(
    Long id,
    String title,
    Long ownerId,
    String ownerUsername,
    Instant createdAt
) {
}