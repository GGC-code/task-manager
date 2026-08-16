package com.taskmanager.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(

    @NotBlank(message = "Lo username è obbligatorio")
    @Size(min = 3, max = 50, message = "Lo username deve avere tra 3 e 50 caratteri")
    String username,

    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "L'email non è valida")
    String email,

    @NotBlank(message = "La password è obbligatoria")
    @Size(min = 8, max = 100, message = "La password deve avere almeno 8 caratteri")
    String password
) {
}