package com.openclassrooms.mddapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.service.ThemeService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/theme")
@RequiredArgsConstructor
@RestController
public class ThemeController {

	private final ThemeService themeService;

	@GetMapping
	public ResponseEntity<?> getAllThemes() {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(themeService.findAll());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("/{themeId}/subscribe")
	public ResponseEntity<?> subscribeToTheme(@PathVariable Long themeId) {
		try {
			themeService.subscribeToTheme(themeId);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	@PutMapping("/{themeId}/unsubscribe")
	public ResponseEntity<?> unsubscribeFromTheme(@PathVariable Long themeId) {
		try {
			themeService.unsubscribeFromTheme(themeId);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
}
