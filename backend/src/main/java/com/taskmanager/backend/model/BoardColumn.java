package com.taskmanager.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "board_columns") // "column" è parola riservata in SQL: uso "board_columns" per la tabella
public class BoardColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100) // il nome della colonna è obbligatorio
    private String name;

    @Column(nullable = false)
    private int position; // indice di ordinamento: definisce la sequenza delle colonne nella bacheca

    // Relazione molti-a-uno: molte colonne appartengono a una sola bacheca.
    // È il lato "COLONNA" della relazione "BACHECA contiene COLONNA" dell'ER.
    @ManyToOne(optional = false) // una colonna DEVE appartenere a una bacheca
    @JoinColumn(name = "board_id", nullable = false) // chiave esterna verso la tabella boards
    private Board board;

    // JPA richiede un costruttore vuoto
    protected BoardColumn() {
    }

    public BoardColumn(String name, int position, Board board) {
        this.name = name;
        this.position = position;
        this.board = board;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}