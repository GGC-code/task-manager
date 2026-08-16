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
import jakarta.persistence.Table;

@Entity //dice a JPA/Hibernate che questa classe corrisponde a una tabella del database
@Table(name = "users") //user si usa in molti databse relazionali ed andrebbe in errore
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // delega al db la generazione dell'identificativo
    private Long id;

    @Column(nullable = false, unique = true, length = 50) // campo obbligatorio senza duplicati
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING) // meglio STRING rispetto ad ORDINAL perchè se si riordina i valori dell'enum i dati salvati restano coerenti con Ordinal si corromperebbero
    @Column(nullable = false, length = 20)
    private Role role;

    @CreationTimestamp // Hibernate crea automaticamente data ed ora di creaione
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // JPA richiede un costruttore vuoto (senza argomenti)
    protected User() {
    }

    public User(String username, String email, String password, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}