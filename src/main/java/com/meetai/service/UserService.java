package com.meetai.service;

import com.meetai.dto.LoginRequestDTO;
import com.meetai.dto.LoginResponseDTO;
import com.meetai.dto.UserRequestDTO;
import com.meetai.dto.UserResponseDTO;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO userRequest);

    UserResponseDTO getUserById(Long id);

    LoginResponseDTO login(LoginRequestDTO loginRequest);
}