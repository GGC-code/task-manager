package com.taskmanager.backend.model;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200) // il titolo è obbligatorio
    private String title;

    @Column(length = 2000) // descrizione facoltativa; VARCHAR ampio per restare portabile tra H2 e PostgreSQL
    private String description;

    @Column(nullable = false)
    private int position; // ordine del task all'interno della colonna

    // Relazione OBBLIGATORIA: ogni task appartiene a una colonna.
    // È il lato "TASK" della relazione "COLONNA contiene TASK" dell'ER.
    @ManyToOne(optional = false)
    @JoinColumn(name = "column_id", nullable = false)
    private BoardColumn boardColumn;

    // Relazione FACOLTATIVA: un task può essere assegnato a un utente, oppure restare non assegnato.
    // È la relazione "assegnatario di" dell'ER, quella che ammette l'assenza di assegnazione.
    @ManyToOne(optional = true)
    @JoinColumn(name = "assignee_id") // senza nullable=false: la colonna può essere vuota
    private User assignee;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // JPA richiede un costruttore vuoto
    protected Task() {
    }

    public Task(String title, String description, int position, BoardColumn boardColumn) {
        this.title = title;
        this.description = description;
        this.position = position;
        this.boardColumn = boardColumn;
        // assignee resta null: il task nasce non assegnato
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public BoardColumn getBoardColumn() {
        return boardColumn;
    }

    // Questo setter servirà per il gesto tipico del Kanban: spostare un task in un'altra colonna.
    public void setBoardColumn(BoardColumn boardColumn) {
        this.boardColumn = boardColumn;
    }

    public User getAssignee() {
        return assignee;
    }

    // Questo setter servirà per assegnare (o riassegnare) il task a un utente.
    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}