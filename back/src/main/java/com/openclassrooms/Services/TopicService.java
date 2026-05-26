package com.openclassrooms.Services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.openclassrooms.DTO.Response.TopicResponseDTO;
import com.openclassrooms.Models.SubscriptionEntity;
import com.openclassrooms.Models.SubscriptionId;
import com.openclassrooms.Models.TopicEntity;
import com.openclassrooms.Models.UserEntity;
import com.openclassrooms.Repositories.SubscriptionRepository;
import com.openclassrooms.Repositories.TopicRepository;
import com.openclassrooms.Repositories.UserRepository;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                                            .getAuthentication()
                                            .getName();  
        return userRepository.findByEmail(email)
                             .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    /* Récupération de tous les thèmes auxquels un utilisateur peut s'abonner */
    public List<TopicResponseDTO> getAllTopics() {
        UserEntity currentUser = getCurrentUser();
        List<TopicEntity> topics = topicRepository.findAll();

        return topics.stream().map(topic -> {
            TopicResponseDTO dto = new TopicResponseDTO();
            dto.setId(topic.getId());
            dto.setName(topic.getName());
            
            SubscriptionId subId = new SubscriptionId();
            subId.setUserId(currentUser.getId());
            subId.setTopicId(topic.getId());
            dto.setSubscribed(subscriptionRepository.existsById(subId));
            return dto;
        }).collect(Collectors.toList());
    }

    /* Abonnement à un thème */
    public void subscribe(Integer topicId) {
    try {
        UserEntity user = getCurrentUser();
        TopicEntity topic = topicRepository.findById(topicId)
            .orElseThrow(() -> new RuntimeException("Thème introuvable"));

        SubscriptionId subId = new SubscriptionId();
        subId.setUserId(user.getId());
        subId.setTopicId(topicId);

        if (!subscriptionRepository.existsById(subId)) {
            SubscriptionEntity subscription = new SubscriptionEntity();
            subscription.setId(subId);
            subscription.setUser(user);
            subscription.setTopic(topic);
            subscriptionRepository.save(subscription);
        }
    } catch (Exception e) {
        System.out.println("ERREUR SUBSCRIBE : " + e.getMessage());
        e.printStackTrace();
        throw e;
    }
}

    /* Désabonnement d'un thème */
    public void unsubscribeFromTopic(Integer topicId) {
        UserEntity currentUser = getCurrentUser();

        SubscriptionId subId = new SubscriptionId();
        subId.setUserId(currentUser.getId());
        subId.setTopicId(topicId);

        subscriptionRepository.deleteById(subId);
    }

    /* Récupération des thèmes auxquels un utilisateur est abonné */
    public List<TopicResponseDTO> getUserSubscriptions() {
        UserEntity currentUser = getCurrentUser();
        List<SubscriptionEntity> subscriptions = subscriptionRepository.findByUser(currentUser);

        return subscriptions.stream().map(sub -> {
            TopicResponseDTO dto = new TopicResponseDTO();
            dto.setId(sub.getTopic().getId());
            dto.setName(sub.getTopic().getName());
            dto.setSubscribed(true);
            return dto;
        }).collect(Collectors.toList());
    }
}
