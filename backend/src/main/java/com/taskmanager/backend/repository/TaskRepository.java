package com.taskmanager.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskmanager.backend.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    // Tutti i task di una colonna, ordinati per posizione crescente.
    // "ColumnId" naviga la relazione Task -> boardColumn (BoardColumn) -> id.
    List<Task> findByBoardColumnIdOrderByPositionAsc(Long columnId);

    // Quanti task ha già una colonna: serve per assegnare la posizione al nuovo task (in fondo).
    long countByBoardColumnId(Long columnId);

    // Esiste almeno un task ancora assegnato a questo utente?
    // Servirà come controllo di sicurezza prima di rimuovere un utente da una bacheca.
    boolean existsByAssigneeId(Long assigneeId);
}
