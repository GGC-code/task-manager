package com.taskmanager.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.backend.dto.AddMemberRequest;
import com.taskmanager.backend.dto.MemberResponse;
import com.taskmanager.backend.service.BoardMembershipService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/boards/{boardId}/members") // i membri vivono "dentro" una bacheca
public class BoardMembershipController {

    private final BoardMembershipService membershipService;

    public BoardMembershipController(BoardMembershipService membershipService) {
        this.membershipService = membershipService;
    }

    @GetMapping // GET /api/boards/{boardId}/members -> elenco dei membri
    public List<MemberResponse> getMembers(@PathVariable Long boardId) {
        return membershipService.getMembers(boardId);
    }

    @PostMapping // POST /api/boards/{boardId}/members -> aggiunge un membro
    public ResponseEntity<MemberResponse> addMember(
            @PathVariable Long boardId,
            @Valid @RequestBody AddMemberRequest request) {
        MemberResponse created = membershipService.addMember(boardId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // 201 Created
    }

    @DeleteMapping("/{userId}") // DELETE /api/boards/{boardId}/members/{userId} -> rimuove un membro
    public ResponseEntity<Void> removeMember(
            @PathVariable Long boardId,
            @PathVariable Long userId) {
        membershipService.removeMember(boardId, userId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}