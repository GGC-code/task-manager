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

@Entity // dice a JPA/Hibernate che questa classe è una tabella del database
@Table(name = "boards") // plurale e in inglese, come "users": nome coerente ed esente da parole riservate
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // l'id lo genera il database
    private Long id;

    @Column(nullable = false, length = 100) // il titolo è obbligatorio
    private String title;

    // Relazione molti-a-uno: molte bacheche appartengono a un solo proprietario (User).
    // È il lato "Board" della relazione uno-a-molti descritta nell'ER.
    @ManyToOne(optional = false) // ogni bacheca DEVE avere un proprietario
    @JoinColumn(name = "owner_id", nullable = false) // la chiave esterna nella tabella boards si chiama owner_id
    private User owner;

    @CreationTimestamp // Hibernate valorizza da solo data e ora alla creazione
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // JPA richiede un costruttore vuoto (senza argomenti)
    protected Board() {
    }

    public Board(String title, User owner) {
        this.title = title;
        this.owner = owner;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
