package com.taskmanager.backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.taskmanager.backend.dto.CreateUserRequest;
import com.taskmanager.backend.dto.UserResponse;
import com.taskmanager.backend.model.Role;
import com.taskmanager.backend.model.User;
import com.taskmanager.backend.repository.UserRepository;

@Service
public class UserService {
    //l'injection delle dipendenze tramite costruttore che spring gli fornirà
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse createUser(CreateUserRequest request) { //controllo con la logica di business con HTTP 409 (conflict)
        if (userRepository.existsByUsername(request.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username già in uso");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email già in uso");
        }

        String hashedPassword = passwordEncoder.encode(request.password());
        User user = new User(request.username(), request.email(), hashedPassword, Role.MEMBER);
        User saved = userRepository.save(user);

        return toResponse(saved);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
            .stream()
            .map(this::toResponse)
            .toList();
    }
    //mappatura entità-DTO traducendo l'entità User nel DTO UserResponse escludendo la password assegnando il ruolo di MEMBER
    private UserResponse toResponse(User user) {
        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole(),
            user.getCreatedAt()
        );
    }
}