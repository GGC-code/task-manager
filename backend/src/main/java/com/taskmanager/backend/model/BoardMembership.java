package com.taskmanager.backend.model;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
// Vincolo di unicità sulla COPPIA (board_id, user_id): un utente non può essere
// iscritto due volte alla stessa bacheca. Traduce l'unicità utente-bacheca dell'ER.
@Table(name = "board_memberships", uniqueConstraints = {
    @UniqueConstraint(name = "uk_board_user", columnNames = {"board_id", "user_id"})
})
public class BoardMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Le due relazioni che collegano un utente a una bacheca: è il cuore dell'entità associativa.
    @ManyToOne(optional = false)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Ruolo che l'utente ricopre in QUESTA bacheca (non è il ruolo generale dell'account).
    @Enumerated(EnumType.STRING)
    @Column(name = "role_on_board", nullable = false, length = 20)
    private Role roleOnBoard;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected BoardMembership() {
    }

    public BoardMembership(Board board, User user, Role roleOnBoard) {
        this.board = board;
        this.user = user;
        this.roleOnBoard = roleOnBoard;
    }

    public Long getId() {
        return id;
    }

    public Board getBoard() {
        return board;
    }

    public User getUser() {
        return user;
    }

    public Role getRoleOnBoard() {
        return roleOnBoard;
    }

    public void setRoleOnBoard(Role roleOnBoard) {
        this.roleOnBoard = roleOnBoard;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}