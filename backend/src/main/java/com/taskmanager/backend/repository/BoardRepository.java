package com.taskmanager.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskmanager.backend.model.Board;
import com.taskmanager.backend.model.User;
//all'avvio Spring genera automaticamente una classe concreta che realizza questi metodi. Tu dichiari cosa vuoi, il framework fornisce il come
public interface BoardRepository extends JpaRepository<Board, Long> {
    // Tutte le bacheche possedute da un certo proprietario.
    // Spring costruisce la query da solo leggendo il nome del metodo: findBy + Owner.
    List<Board> findByOwner(User owner);

    // Stessa cosa, ma partendo dall'id dell'utente invece che dall'oggetto User.
    // "OwnerId" naviga la relazione: Board -> owner (User) -> id.
    List<Board> findByOwnerId(Long ownerId);
}
