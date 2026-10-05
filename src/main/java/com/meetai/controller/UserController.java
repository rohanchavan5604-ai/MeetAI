package com.meetai.controller;

import com.meetai.dto.LoginRequestDTO;
import com.meetai.dto.LoginResponseDTO;
import com.meetai.dto.UserRequestDTO;
import com.meetai.dto.UserResponseDTO;
import com.meetai.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public UserResponseDTO createUser(
            @Valid @RequestBody UserRequestDTO userRequest) {

        return userService.createUser(userRequest);
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUserById(
            @PathVariable Long id) {

        return userService.getUserById(id);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO loginRequest) {

        return ResponseEntity.ok(
                userService.login(loginRequest)
        );
    }
}