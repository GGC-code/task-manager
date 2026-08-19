package com.taskmanager.backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.taskmanager.backend.dto.AddMemberRequest;
import com.taskmanager.backend.dto.MemberResponse;
import com.taskmanager.backend.model.Board;
import com.taskmanager.backend.model.BoardMembership;
import com.taskmanager.backend.model.Role;
import com.taskmanager.backend.model.User;
import com.taskmanager.backend.repository.BoardMembershipRepository;
import com.taskmanager.backend.repository.BoardRepository;
import com.taskmanager.backend.repository.UserRepository;

@Service
public class BoardMembershipService {

    private final BoardMembershipRepository membershipRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public BoardMembershipService(BoardMembershipRepository membershipRepository,
                                  BoardRepository boardRepository,
                                  UserRepository userRepository) {
        this.membershipRepository = membershipRepository;
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    public MemberResponse addMember(Long boardId, AddMemberRequest request) {
        // 1) La bacheca deve esistere.
        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Bacheca non trovata"));

        // 2) L'utente da aggiungere deve esistere.
        User user = userRepository.findById(request.userId())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Utente non trovato"));

        // 3) Anti-doppione: la coppia non deve essere già iscritta -> 409 Conflict.
        if (membershipRepository.existsByBoardIdAndUserId(boardId, request.userId())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT, "L'utente è già membro di questa bacheca");
        }

        // Ruolo di default sulla bacheca: MEMBER. Il proprietario resta tale tramite owner della bacheca.
        BoardMembership membership = new BoardMembership(board, user, Role.MEMBER);
        BoardMembership saved = membershipRepository.save(membership);

        return toResponse(saved);
    }

    public List<MemberResponse> getMembers(Long boardId) {
        if (!boardRepository.existsById(boardId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bacheca non trovata");
        }
        return membershipRepository.findByBoardId(boardId)
            .stream()
            .map(this::toResponse)
            .toList();
    }

    public void removeMember(Long boardId, Long userId) {
        // Cerchiamo l'iscrizione specifica della coppia; se non c'è, 404.
        BoardMembership membership = membershipRepository.findByBoardIdAndUserId(boardId, userId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Iscrizione non trovata"));
        membershipRepository.delete(membership);
    }

    private MemberResponse toResponse(BoardMembership membership) {
        return new MemberResponse(
            membership.getId(),
            membership.getUser().getId(),
            membership.getUser().getUsername(),
            membership.getRoleOnBoard()
        );
    }
}
