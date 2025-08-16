package com.openclassrooms.mddapi.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.dto.SubscriptionResponse;
import com.openclassrooms.mddapi.model.Subscription;
import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

	private final SubscriptionRepository subscriptionRepository;
	private final ThemeRepository themeRepository;
	private final UserRepository userRepository;

	@Override
	public List<SubscriptionResponse> getUserSubscriptions() {
		User currentUser = getCurrentUser();
		List<Subscription> subscriptions = subscriptionRepository.findByUser(currentUser);

		return subscriptions.stream()
				.map(sub -> SubscriptionResponse.builder().themeId(sub.getTheme().getId())
						.themeName(sub.getTheme().getName()).themeDescription(sub.getTheme().getDescription()).build())
				.collect(Collectors.toList());
	}

	@Override
	public void unsubscribe(Long themeId) {
		User currentUser = getCurrentUser();
		Theme theme = themeRepository.findById(themeId).orElseThrow(() -> new RuntimeException("Thème non trouvé"));

		Subscription subscription = subscriptionRepository.findByUserAndTheme(currentUser, theme)
				.orElseThrow(() -> new RuntimeException("Abonnement non trouvé"));

		subscriptionRepository.delete(subscription);
	}

	private User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		return userRepository.findByEmailOrUsername(username, username)
				.orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
	}
}
