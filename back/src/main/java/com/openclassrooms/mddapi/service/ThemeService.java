package com.openclassrooms.mddapi.service;

import java.util.List;

import com.openclassrooms.mddapi.dto.ThemeResponse;

public interface ThemeService {

	List<ThemeResponse> findAll();
	void subscribeToTheme(Long themeId);
	void unsubscribeFromTheme(Long themeId);
}
