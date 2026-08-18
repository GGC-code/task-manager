package com.taskmanager.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTaskRequest(

    @NotBlank(message = "Il titolo del task è obbligatorio")
    @Size(max = 200, message = "Il titolo può avere al massimo 200 caratteri")
    String title,

    @Size(max = 2000, message = "La descrizione può avere al massimo 2000 caratteri")
    String description
) {
}
