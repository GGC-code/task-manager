package com.taskmanager.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateBoardColumnRequest(

    @NotBlank(message = "Il nome della colonna è obbligatorio")
    @Size(max = 100, message = "Il nome può avere al massimo 100 caratteri")
    String name
) {
}