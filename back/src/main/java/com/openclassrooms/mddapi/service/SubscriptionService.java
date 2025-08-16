package com.openclassrooms.mddapi.service;

import java.util.List;

import com.openclassrooms.mddapi.dto.SubscriptionResponse;

public interface SubscriptionService {
	List<SubscriptionResponse> getUserSubscriptions();

	void unsubscribe(Long themeId);
}