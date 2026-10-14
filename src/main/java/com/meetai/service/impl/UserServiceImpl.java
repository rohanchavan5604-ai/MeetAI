package com.meetai.service.impl;

import com.meetai.dto.*;
import com.meetai.entity.User;
import com.meetai.exception.EmailAlreadyExistsException;
import com.meetai.exception.InvalidPasswordException;
import com.meetai.exception.UserNotFoundException;
import com.meetai.repository.UserRepository;
import com.meetai.security.JwtUtil;
import com.meetai.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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

    // Register a new user
    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequest) {

        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException(
                    "Email already registered"
            );
        }

        User user = new User();

        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());

        // Encrypt password using BCrypt
        user.setPassword(
                passwordEncoder.encode(userRequest.getPassword())
        );

        User savedUser = userRepository.save(user);

        return convertToResponse(savedUser);
    }

    // Get user by ID
    @Override
    public UserResponseDTO getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + id
                        )
                );

        return convertToResponse(user);
    }

    // Login user
    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {

        User user = userRepository
                .findByEmail(loginRequest.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid email or password"
                        )
                );

        if (!passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPassword())) {

            throw new RuntimeException(
                    "Invalid email or password"
            );
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

    // Get logged-in user's profile
    @Override
    public UserResponseDTO getMyProfile() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with email: " + email
                        )
                );

        return convertToResponse(user);
    }

    // Update logged-in user's profile
    @Override
    public UserResponseDTO updateMyProfile(
            UserProfileUpdateDTO profileUpdateDTO) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with email: " + email
                        )
                );

        user.setName(profileUpdateDTO.getName());

        User updatedUser = userRepository.save(user);

        return convertToResponse(updatedUser);
    }

    // Change password for the logged-in user
    @Override
    public void changePassword(
            ChangePasswordRequestDTO changePasswordRequest) {

        // Get logged-in user's email from JWT authentication
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        // Find the logged-in user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with email: " + email
                        )
                );

        // Verify current password
        if (!passwordEncoder.matches(
                changePasswordRequest.getCurrentPassword(),
                user.getPassword())) {

            throw new InvalidPasswordException(
                    "Current password is incorrect"
            );
        }

        // Prevent using the same password again
        if (passwordEncoder.matches(
                changePasswordRequest.getNewPassword(),
                user.getPassword())) {

            throw new InvalidPasswordException(
                    "New password must be different from current password"
            );
        }

        // Encode and save the new password
        user.setPassword(
                passwordEncoder.encode(
                        changePasswordRequest.getNewPassword()
                )
        );

        userRepository.save(user);
    }

    // Convert User entity to UserResponseDTO
    private UserResponseDTO convertToResponse(User user) {

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}