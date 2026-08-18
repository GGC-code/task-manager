package com.taskmanager.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskmanager.backend.model.BoardColumn;

public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {
    // Tutte le colonne di una bacheca, già ordinate per posizione crescente.
    // "OrderByPositionAsc" aggiunge da solo la clausola ORDER BY position ASC.
    List<BoardColumn> findByBoardIdOrderByPositionAsc(Long boardId);

    // Quante colonne ha già una bacheca: serve per assegnare la posizione
    // alla prossima colonna creata, così da metterla in fondo.
    long countByBoardId(Long boardId);
}
