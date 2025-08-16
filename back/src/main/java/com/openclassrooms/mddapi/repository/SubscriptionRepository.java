package com.openclassrooms.mddapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.mddapi.model.Subscription;
import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.model.User;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

	List<Subscription> findByUser(User user);

	boolean existsByUserIdAndThemeId(Long userId, Long themeId);
	
	Optional<Subscription> findByUserAndTheme(User user, Theme theme);

	boolean existsByUserAndTheme(User currentUser, Theme theme);

}