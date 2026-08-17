package com.taskmanager.backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.taskmanager.backend.dto.BoardResponse;
import com.taskmanager.backend.dto.CreateBoardRequest;
import com.taskmanager.backend.model.Board;
import com.taskmanager.backend.model.User;
import com.taskmanager.backend.repository.BoardRepository;
import com.taskmanager.backend.repository.UserRepository;

@Service
public class BoardService {

    // Due dipendenze: il repository delle bacheche e quello degli utenti.
    // Serve anche UserRepository perché per creare una bacheca dobbiamo
    // recuperare l'utente che ne diventa proprietario.
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public BoardService(BoardRepository boardRepository, UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    public BoardResponse createBoard(CreateBoardRequest request) {
        // Carichiamo il proprietario dal database a partire dall'id ricevuto.
        // Se non esiste, rispondiamo 404 invece di lasciar esplodere un errore generico.
        User owner = userRepository.findById(request.ownerId())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Utente proprietario non trovato"));

        Board board = new Board(request.title(), owner);
        Board saved = boardRepository.save(board);

        return toResponse(saved);
    }

    public List<BoardResponse> getAllBoards() {
        return boardRepository.findAll()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    public BoardResponse getBoardById(Long id) {
        Board board = boardRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Bacheca non trovata"));
        return toResponse(board);
    }

    // Mappatura entità -> DTO: appiattisce il proprietario a id + username,
    // così la risposta non trascina mai fuori l'intero oggetto User.
    private BoardResponse toResponse(Board board) {
        return new BoardResponse(
            board.getId(),
            board.getTitle(),
            board.getOwner().getId(),
            board.getOwner().getUsername(),
            board.getCreatedAt()
        );
    }
}