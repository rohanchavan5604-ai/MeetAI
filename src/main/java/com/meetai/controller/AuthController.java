package com.meetai.controller;

import com.meetai.dto.LoginRequestDTO;
import com.meetai.dto.LoginResponseDTO;
import com.meetai.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public LoginResponseDTO login(
            @Valid @RequestBody LoginRequestDTO loginRequest) {

        return userService.login(loginRequest);
    }
}