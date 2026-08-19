package com.taskmanager.backend.dto;

import com.taskmanager.backend.model.Role;

// Rappresenta un membro di una bacheca. Include i dati utili dell'utente
// (id e username) più il ruolo che ricopre in quella bacheca.
public record MemberResponse(
    Long membershipId,
    Long userId,
    String username,
    Role roleOnBoard
) {
}