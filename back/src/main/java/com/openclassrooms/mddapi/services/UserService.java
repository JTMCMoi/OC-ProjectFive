package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.UserRegisterDto;
import com.openclassrooms.mddapi.dto.UserResponseDto;
import com.openclassrooms.mddapi.models.UserEntity;
import com.openclassrooms.mddapi.repositorys.UserRepository;
import com.openclassrooms.mddapi.services.mappers.UserMapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Crée un nouvel utilisateur à partir d’un DTO d’inscription.
     */
    public void createUser(UserRegisterDto request) {

        if (userRepository.existsByEmail(request.email())
            || userRepository.existsByUsername(request.username())) {
            throw new IllegalStateException("Error: Email or Username is already taken!");
        }

        validatePassword(request.password());

        UserEntity user = UserEntity.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();

        userRepository.save(user);
    }

    /**
     * Vérifie la robustesse du mot de passe avec une regex.
     */
    private void validatePassword(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        if (!password.matches(regex)) {
            throw new IllegalArgumentException(
                "Le mot de passe doit contenir au moins une majuscule, une minuscule, un chiffre et un caractère spécial"
            );
        }
    }

    /**
     * Récupère un utilisateur par son ID (DTO).
     */
    public Optional<UserResponseDto> findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto);
    }

    /**
     * Récupère un utilisateur par son email (DTO).
     */
    public Optional<UserResponseDto> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDto);
    }

    /**
     * Récupère un utilisateur par son username (DTO).
     */
    public Optional<UserResponseDto> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toDto);
    }

    /**
     * Vérifie si un utilisateur existe par email.
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Vérifie si un utilisateur existe par username.
     */
    public boolean existsByUserName(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Supprime un utilisateur par son ID.
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
