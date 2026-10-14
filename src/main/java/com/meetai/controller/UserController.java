package com.meetai.controller;

import com.meetai.dto.*;
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

    // Register a new user
    @PostMapping
    public UserResponseDTO createUser(
            @Valid @RequestBody UserRequestDTO userRequest) {

        return userService.createUser(userRequest);
    }

    // Get user by ID
    @GetMapping("/{id}")
    public UserResponseDTO getUserById(
            @PathVariable Long id) {

        return userService.getUserById(id);
    }

    // Login user
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO loginRequest) {

        return ResponseEntity.ok(
                userService.login(loginRequest)
        );
    }

    // Get logged-in user's profile
    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getMyProfile() {

        return ResponseEntity.ok(
                userService.getMyProfile()
        );

    }
    // Update logged-in user's profile
    @PutMapping("/profile")
    public ResponseEntity<UserResponseDTO> updateMyProfile(
            @Valid @RequestBody UserProfileUpdateDTO profileUpdateDTO) {

        return ResponseEntity.ok(
                userService.updateMyProfile(profileUpdateDTO)
        );
    }

    // Change password for logged-in user
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @Valid @RequestBody ChangePasswordRequestDTO changePasswordRequest) {

        userService.changePassword(changePasswordRequest);

        return ResponseEntity.ok("Password changed successfully");
    }

}