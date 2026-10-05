package com.meetai.service.impl;

import com.meetai.dto.LoginRequestDTO;
import com.meetai.dto.LoginResponseDTO;
import com.meetai.dto.UserRequestDTO;
import com.meetai.dto.UserResponseDTO;
import com.meetai.entity.User;
import com.meetai.exception.EmailAlreadyExistsException;
import com.meetai.exception.UserNotFoundException;
import com.meetai.repository.UserRepository;
import com.meetai.security.JwtUtil;
import com.meetai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequest) {

        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        User user = new User();

        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());

        user.setPassword(
                passwordEncoder.encode(userRequest.getPassword())
        );

        User savedUser = userRepository.save(user);

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    @Override
    public UserResponseDTO getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + id
                        )
                );

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {

        User user = userRepository
                .findByEmail(loginRequest.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password")
                );

        if (!passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPassword())) {

            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return new LoginResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                token
        );
    }
}