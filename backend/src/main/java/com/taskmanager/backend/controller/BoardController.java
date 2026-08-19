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

import com.taskmanager.backend.dto.BoardResponse;
import com.taskmanager.backend.dto.CreateBoardRequest;
import com.taskmanager.backend.security.SecurityUtils;
import com.taskmanager.backend.service.BoardService;


import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/boards") // tutti gli endpoint di questa classe partono da /api/boards
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    public ResponseEntity<BoardResponse> createBoard(@Valid @RequestBody CreateBoardRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId(); // proprietario = utente autenticato
        BoardResponse created = boardService.createBoard(request, currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping // GET /api/boards -> elenco di tutte le bacheche
    public List<BoardResponse> getAllBoards() {
        return boardService.getAllBoards();
    }

    @GetMapping("/{id}") // GET /api/boards/{id} -> una bacheca specifica
    public BoardResponse getBoardById(@PathVariable Long id) {
        return boardService.getBoardById(id);
    }
}