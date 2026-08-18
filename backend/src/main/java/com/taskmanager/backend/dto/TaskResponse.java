package com.taskmanager.backend.dto;

import java.time.Instant;

// Appiattisce entrambe le relazioni: la colonna al solo columnId,
// l'assegnatario a id e username (che possono essere null se il task non è assegnato).
public record TaskResponse(
    Long id,
    String title,
    String description,
    int position,
    Long columnId,
    Long assigneeId,
    String assigneeUsername,
    Instant createdAt
) {
}
