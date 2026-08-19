package com.taskmanager.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.backend.dto.CreateTaskRequest;
import com.taskmanager.backend.dto.TaskResponse;
import com.taskmanager.backend.dto.UpdateTaskRequest;
import com.taskmanager.backend.service.TaskService;

import jakarta.validation.Valid;

@RestController // niente @RequestMapping di classe: qui i percorsi sono due, li mettiamo sui singoli metodi
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Annidati sotto la colonna: creazione ed elenco dei task di una colonna.
    @GetMapping("/api/columns/{columnId}/tasks")
    public List<TaskResponse> getTasks(@PathVariable Long columnId) {
        return taskService.getTasksByColumn(columnId);
    }

    @PostMapping("/api/columns/{columnId}/tasks")
    public ResponseEntity<TaskResponse> createTask(
            @PathVariable Long columnId,
            @Valid @RequestBody CreateTaskRequest request) {
        TaskResponse created = taskService.createTask(columnId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // 201 Created
    }

    // Sul singolo task: modifica (contenuti, spostamento, assegnazione) ed eliminazione.
    @PutMapping("/api/tasks/{id}")
    public TaskResponse updateTask(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskRequest request) {
        return taskService.updateTask(id, request);
    }

    @DeleteMapping("/api/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
