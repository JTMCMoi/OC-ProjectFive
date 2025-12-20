package com.openclassrooms.mddapi.repositorys;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.openclassrooms.mddapi.models.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {


    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM UserEntity u WHERE u.email = :identifier OR u.username = :identifier")
    Optional<UserEntity> findByEmailOrUsername(@Param("identifier") String identifier);

    @Query("SELECT u FROM UserEntity u WHERE u.email = :identifier OR u.username = :identifier")
    boolean existByEmailOrUsername(@Param("identifier") String identifier);

}
