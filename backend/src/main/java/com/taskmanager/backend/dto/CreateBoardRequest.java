package com.taskmanager.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
//ponte temporaneo per ora chi hiama l'API deve dirci esplicitamente di chi è la bacheca
public record CreateBoardRequest(
    //controlla che siano stringhe vuote o fatte solo da spazi
    @NotBlank(message = "Il titolo è obbligatorio")
    @Size(min = 1, max = 100, message = "Il titolo può avere al massimo 100 caratteri")
    String title,

    // TEMPORANEO: finché non c'è l'autenticazione JWT, il proprietario va indicato
    // qui esplicitamente. Nel Capitolo 3 questo campo sparirà e il proprietario
    // verrà ricavato dall'utente autenticato (dal token).
    @NotNull(message = "L'id del proprietario è obbligatorio")
    Long ownerId
) {
}