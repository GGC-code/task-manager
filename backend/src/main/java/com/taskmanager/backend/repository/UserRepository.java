package com.taskmanager.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskmanager.backend.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    //query specifiche per username ed email per gestire l'assenza di un risultato evitando null
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}