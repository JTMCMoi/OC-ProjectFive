package com.openclassrooms.mddapi.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openclassrooms.mddapi.dto.subscription.SubscriptionResponseDto;
import com.openclassrooms.mddapi.exceptions.ResourceNotFoundException;
import com.openclassrooms.mddapi.models.SubscriptionEntity;
import com.openclassrooms.mddapi.models.TopicEntity;
import com.openclassrooms.mddapi.models.UserEntity;
import com.openclassrooms.mddapi.repositorys.SubscriptionRepository;
import com.openclassrooms.mddapi.repositorys.TopicRepository;
import com.openclassrooms.mddapi.repositorys.UserRepository;
import com.openclassrooms.mddapi.services.mappers.SubscriptionMapper;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionService {

    private final SubscriptionRepository repo;
    private final TopicRepository topicRepo;
    private final UserRepository userRepo;
    private final AuthenticationService auth;
    private final SubscriptionMapper mapper;  

    public SubscriptionResponseDto subscribe(Long topicId) {

        String identifier = auth.getAuthenticatedIdentifier();

        UserEntity user = userRepo.findByEmailOrUsername(identifier)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        TopicEntity topic = topicRepo.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));

        if (repo.existsByUserIdAndTopicId(user.getId(), topicId)) {
            throw new IllegalStateException("Already subscribed");
        }

        SubscriptionEntity s = new SubscriptionEntity();
        s.setUser(user);
        s.setTopic(topic);

        return mapper.toDto(repo.save(s));
    }

    public void unsubscribe(Long topicId) {

        String identifier = auth.getAuthenticatedIdentifier();

        UserEntity user = userRepo.findByEmailOrUsername(identifier)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        SubscriptionEntity sub = repo.findByUserIdAndTopicId(user.getId(), topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));

        repo.delete(sub);
    }

    public List<SubscriptionResponseDto> getMySubscriptions() {

        String identifier = auth.getAuthenticatedIdentifier();

        UserEntity user = userRepo.findByEmailOrUsername(identifier)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return repo.findByUserId(user.getId())
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}
