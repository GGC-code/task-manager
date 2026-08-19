package com.taskmanager.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskmanager.backend.model.BoardMembership;

public interface BoardMembershipRepository extends JpaRepository<BoardMembership, Long> {
    // Tutti i membri di una bacheca (le iscrizioni relative a quella bacheca).
    List<BoardMembership> findByBoardId(Long boardId);

    // Tutte le iscrizioni di un utente (le bacheche a cui partecipa).
    List<BoardMembership> findByUserId(Long userId);

    // Esiste già l'iscrizione di QUESTO utente a QUESTA bacheca?
    // Serve per non creare doppioni prima ancora che scatti il vincolo del database.
    boolean existsByBoardIdAndUserId(Long boardId, Long userId);

    // La singola iscrizione di un utente a una bacheca: serve per rimuovere un membro.
    Optional<BoardMembership> findByBoardIdAndUserId(Long boardId, Long userId);
}