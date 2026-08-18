package com.taskmanager.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.backend.dto.BoardColumnResponse;
import com.taskmanager.backend.dto.CreateBoardColumnRequest;
import com.taskmanager.backend.service.BoardColumnService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/boards/{boardId}/columns") // le colonne vivono "dentro" una bacheca
public class BoardColumnController {

    private final BoardColumnService columnService;

    public BoardColumnController(BoardColumnService columnService) {
        this.columnService = columnService;
    }

    @GetMapping // GET /api/boards/{boardId}/columns -> colonne della bacheca, ordinate
    public List<BoardColumnResponse> getColumns(@PathVariable Long boardId) {
        return columnService.getColumnsByBoard(boardId);
    }

    @PostMapping // POST /api/boards/{boardId}/columns -> crea una colonna nella bacheca
    public ResponseEntity<BoardColumnResponse> createColumn(
            @PathVariable Long boardId,
            @Valid @RequestBody CreateBoardColumnRequest request) {
        BoardColumnResponse created = columnService.createColumn(boardId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // 201 Created
    }
}