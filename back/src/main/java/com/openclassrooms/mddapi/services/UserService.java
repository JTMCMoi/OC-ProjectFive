package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.auth.UserRegisterDto;
import com.openclassrooms.mddapi.dto.auth.UserResponseDto;
import com.openclassrooms.mddapi.dto.user.UserUpdateDto;
import com.openclassrooms.mddapi.models.UserEntity;
import com.openclassrooms.mddapi.repositorys.UserRepository;
import com.openclassrooms.mddapi.services.mappers.UserMapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public UserResponseDto updateUser(Long id, UserUpdateDto dto) {

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Vérifie si email unique
        if (dto.email() != null &&
            !dto.email().equals(user.getEmail()) &&
            userRepository.existsByEmail(dto.email())) {

            throw new IllegalStateException("Email déjà utilisé !");
        }

        // Vérifie si username unique
        if (dto.username() != null &&
            !dto.username().equals(user.getUsername()) &&
            userRepository.existsByUsername(dto.username())) {

            throw new IllegalStateException("Nom d'utilisateur déjà utilisé !");
        }

        // Applique les modifications simples
        userMapper.updateEntityFromDto(dto, user);

        // Mise à jour du password
        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        }

        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        return userMapper.toDto(user);
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
     * Récupère un utilisateur par son identité (DTO).
     */
    public Optional<UserResponseDto> findByEmailOrUsername(String identity) {
        return userRepository.findByEmailOrUsername(identity)
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
     * Vérifie si un utilisateur existe par username ou son email.
     */
    public boolean existsByEmailOrUserName(String identify) {
        return userRepository.existByEmailOrUsername(identify);
    }

    /**
     * Supprime un utilisateur par son ID.
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
