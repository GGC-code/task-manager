package com.taskmanager.backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.taskmanager.backend.dto.BoardColumnResponse;
import com.taskmanager.backend.dto.CreateBoardColumnRequest;
import com.taskmanager.backend.model.Board;
import com.taskmanager.backend.model.BoardColumn;
import com.taskmanager.backend.repository.BoardColumnRepository;
import com.taskmanager.backend.repository.BoardRepository;

@Service
public class BoardColumnService {

    // Servono due repository: quello delle colonne per salvarle e leggerle,
    // e quello delle bacheche per verificare che la bacheca "genitore" esista.
    private final BoardColumnRepository columnRepository;
    private final BoardRepository boardRepository;

    public BoardColumnService(BoardColumnRepository columnRepository, BoardRepository boardRepository) {
        this.columnRepository = columnRepository;
        this.boardRepository = boardRepository;
    }

    public BoardColumnResponse createColumn(Long boardId, CreateBoardColumnRequest request) {
        // La bacheca deve esistere: se non c'è, 404.
        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Bacheca non trovata"));

        // La posizione non arriva dal client: la nuova colonna va in fondo.
        // Se la bacheca ha già 3 colonne (0,1,2), il conteggio è 3 = prossima posizione.
        long numeroColonne = columnRepository.countByBoardId(boardId);
        int posizione = (int) numeroColonne;

        BoardColumn column = new BoardColumn(request.name(), posizione, board);
        BoardColumn saved = columnRepository.save(column);

        return toResponse(saved);
    }

    public List<BoardColumnResponse> getColumnsByBoard(Long boardId) {
        // Se la bacheca non esiste, è corretto segnalarlo invece di restituire una lista vuota ambigua.
        if (!boardRepository.existsById(boardId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bacheca non trovata");
        }
        // Colonne già ordinate per posizione, grazie al metodo del repository.
        return columnRepository.findByBoardIdOrderByPositionAsc(boardId)
            .stream()
            .map(this::toResponse)
            .toList();
    }

    // Mappatura entità -> DTO: appiattisce la bacheca al solo boardId.
    private BoardColumnResponse toResponse(BoardColumn column) {
        return new BoardColumnResponse(
            column.getId(),
            column.getName(),
            column.getPosition(),
            column.getBoard().getId()
        );
    }
}