package com.taskmanager.backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.taskmanager.backend.dto.CreateTaskRequest;
import com.taskmanager.backend.dto.TaskResponse;
import com.taskmanager.backend.dto.UpdateTaskRequest;
import com.taskmanager.backend.model.BoardColumn;
import com.taskmanager.backend.model.Task;
import com.taskmanager.backend.repository.BoardColumnRepository;
import com.taskmanager.backend.repository.TaskRepository;

import com.taskmanager.backend.model.User;

@Service
public class TaskService {

    // Tre repository: i task (salvare/leggere), le colonne (verificare la colonna "genitore"
    // e per lo spostamento) e gli utenti (per l'assegnazione). Li dichiariamo già tutti e tre
    // perché serviranno nella seconda parte, con l'aggiornamento.
    private final TaskRepository taskRepository;
    private final BoardColumnRepository columnRepository;
    private final com.taskmanager.backend.repository.UserRepository userRepository;

    public TaskService(TaskRepository taskRepository,
                       BoardColumnRepository columnRepository,
                       com.taskmanager.backend.repository.UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.columnRepository = columnRepository;
        this.userRepository = userRepository;
    }

    public TaskResponse createTask(Long columnId, CreateTaskRequest request) {
        // La colonna deve esistere: altrimenti 404.
        BoardColumn column = columnRepository.findById(columnId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Colonna non trovata"));

        // Posizione calcolata: il nuovo task va in fondo alla colonna.
        int posizione = (int) taskRepository.countByBoardColumnId(columnId);

        Task task = new Task(request.title(), request.description(), posizione, column);
        Task saved = taskRepository.save(task);

        return toResponse(saved);
    }

    public List<TaskResponse> getTasksByColumn(Long columnId) {
        
        // Se la colonna non esiste, lo segnaliamo invece di restituire una lista vuota ambigua.
        if (!columnRepository.existsById(columnId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Colonna non trovata");
        }
        return taskRepository.findByBoardColumnIdOrderByPositionAsc(columnId)
            .stream()
            .map(this::toResponse)
            .toList();
    }

        public TaskResponse updateTask(Long taskId, UpdateTaskRequest request) {
        // Il task deve esistere.
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Task non trovato"));

        // 1) Modifica dei contenuti: aggiorniamo solo i campi effettivamente inviati.
        if (request.title() != null) {
            task.setTitle(request.title());
        }
        if (request.description() != null) {
            task.setDescription(request.description());
        }

        // 2) Spostamento in un'altra colonna (gesto tipico del Kanban).
        if (request.columnId() != null) {
            BoardColumn nuovaColonna = columnRepository.findById(request.columnId())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Colonna di destinazione non trovata"));
            // Il task va in fondo alla colonna di destinazione: nuova posizione = numero di task già presenti.
            int nuovaPosizione = (int) taskRepository.countByBoardColumnId(nuovaColonna.getId());
            task.setBoardColumn(nuovaColonna);
            task.setPosition(nuovaPosizione);
        }

        // 3) Assegnazione a un utente (o rimozione dell'assegnazione con assigneeId = 0).
        if (request.assigneeId() != null) {
            if (request.assigneeId() == 0) {
                // Convenzione: 0 significa "togli l'assegnazione", il task torna non assegnato.
                task.setAssignee(null);
            } else {
                User assegnatario = userRepository.findById(request.assigneeId())
                    .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Utente assegnatario non trovato"));
                task.setAssignee(assegnatario);
            }
        }

        Task salvato = taskRepository.save(task);
        return toResponse(salvato);
    }

        public void deleteTask(Long taskId) {
        // Verifichiamo l'esistenza per poter rispondere 404 se il task non c'è,
        // invece di lasciare che l'eliminazione di un id inesistente passi in silenzio.
        if (!taskRepository.existsById(taskId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task non trovato");
        }
        taskRepository.deleteById(taskId);
    }

    // Mappatura entità -> DTO. Attenzione all'assegnatario: può essere null,
    // quindi lo leggiamo solo se presente, altrimenti lasciamo null nei campi relativi.
    private TaskResponse toResponse(Task task) {
        Long assigneeId = null;
        String assigneeUsername = null;
        if (task.getAssignee() != null) {
            assigneeId = task.getAssignee().getId();
            assigneeUsername = task.getAssignee().getUsername();
        }
        return new TaskResponse(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getPosition(),
            task.getBoardColumn().getId(),
            assigneeId,
            assigneeUsername,
            task.getCreatedAt()
        );
    }
}