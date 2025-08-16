package com.openclassrooms.mddapi.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.dto.ThemeResponse;
import com.openclassrooms.mddapi.model.Subscription;
import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ThemeServiceImpl implements ThemeService {

	private final ThemeRepository themeRepo;
	private final SubscriptionRepository subscriptionRepo;
	private final UserRepository userRepo;

	@Override
	public List<ThemeResponse> findAll() {
		User currentUser = this.getCurrentUser();
		List<Theme> foundedThemes = themeRepo.findAll();
		Set<Long> subscribedThemeIds = subscriptionRepo.findByUser(currentUser).stream().map(s -> s.getTheme().getId())
				.collect(Collectors.toSet());
		return foundedThemes.stream()
				.map(theme -> ThemeResponse.builder().id(theme.getId()).name(theme.getName())
						.description(theme.getDescription()).isSubscribed(subscribedThemeIds.contains(theme.getId()))
						.build())
				.collect(Collectors.toList());
	}

	@Override
	public void subscribeToTheme(Long themeId) {
	    User currentUser = getCurrentUser();
	    Theme theme = themeRepo.findById(themeId)
	        .orElseThrow(() -> new RuntimeException("Thème non trouvé"));

	    boolean alreadySubscribed = subscriptionRepo.existsByUserAndTheme(currentUser, theme);
	    if (!alreadySubscribed) {
	        Subscription subscription = new Subscription(currentUser, theme);
	        subscriptionRepo.save(subscription);
	    }
	}

	@Override
	public void unsubscribeFromTheme(Long themeId) {
	    User currentUser = getCurrentUser();
	    Theme theme = themeRepo.findById(themeId)
	        .orElseThrow(() -> new RuntimeException("Thème non trouvé"));

	    Subscription subscription = subscriptionRepo.findByUserAndTheme(currentUser, theme)
	        .orElseThrow(() -> new RuntimeException("Abonnement non trouvé"));

	    subscriptionRepo.delete(subscription);
	}
	
	private User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return this.userRepo.findByEmailOrUsername(authentication.getName(), authentication.getName())
				.orElseThrow(() -> new RuntimeException("utilisateur introuvable ou non identifier"));
	}
}
