package com.openclassrooms.mddapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.mddapi.model.Theme;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {
	Optional<Theme> findByName(String name);
}