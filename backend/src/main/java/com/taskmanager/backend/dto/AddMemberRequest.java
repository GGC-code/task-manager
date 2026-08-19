package com.taskmanager.backend.dto;

import jakarta.validation.constraints.NotNull;

// Per aggiungere un membro a una bacheca serve solo l'id dell'utente da iscrivere.
// La bacheca arriva dall'URL (endpoint annidato); il ruolo lo impostiamo noi lato server.
public record AddMemberRequest(

    @NotNull(message = "L'id dell'utente è obbligatorio")
    Long userId
) {
}