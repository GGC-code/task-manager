package com.taskmanager.backend.dto;

// Appiattisce la relazione con la bacheca al solo boardId.
public record BoardColumnResponse(
    Long id,
    String name,
    int position,
    Long boardId
) {
}
