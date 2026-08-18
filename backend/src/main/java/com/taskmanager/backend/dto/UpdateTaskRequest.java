package com.taskmanager.backend.dto;

import jakarta.validation.constraints.Size;

// Richiesta di aggiornamento: ogni campo è FACOLTATIVO.
// Il client invia solo ciò che vuole cambiare; i campi lasciati a null restano invariati.
public record UpdateTaskRequest(

    @Size(max = 200, message = "Il titolo può avere al massimo 200 caratteri")
    String title,

    @Size(max = 2000, message = "La descrizione può avere al massimo 2000 caratteri")
    String description,

    // Per spostare il task in un'altra colonna: id della colonna di destinazione.
    Long columnId,

    // Per assegnare il task a un utente: id dell'utente assegnatario.
    Long assigneeId
) {
}
