package com.openclassrooms.mddapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.mddapi.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmailOrUsername(String email, String username);

	boolean existsByEmailOrUsername(String email, String username);

	boolean existsByEmailOrUsernameAndIdNot(String email, String username, Long excludedUserId);
}