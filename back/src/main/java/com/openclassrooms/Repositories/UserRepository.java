package com.openclassrooms.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.Models.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsername(String username);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    /* Cherche l'email ou le nom d'utilisateur */
    Optional<UserEntity> findByEmailOrUsername(String email, String username);
}
