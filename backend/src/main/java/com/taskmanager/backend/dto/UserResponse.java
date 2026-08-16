package com.taskmanager.backend.dto;

import java.time.Instant;

import com.taskmanager.backend.model.Role;
//non ritorna mai la password perchè non lo contiene
public record UserResponse(
    Long id,
    String username,
    String email,
    Role role,
    Instant createdAt
) {
}