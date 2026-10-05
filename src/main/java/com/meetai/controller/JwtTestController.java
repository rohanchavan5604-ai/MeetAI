package com.meetai.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtTestController {

    @GetMapping("/api/protected")
    public String protectedEndpoint() {
        return "JWT Authentication Successful!";
    }
}