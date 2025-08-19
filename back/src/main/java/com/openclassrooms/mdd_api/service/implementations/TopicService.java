package com.openclassrooms.mdd_api.service.implementations;

import com.openclassrooms.mdd_api.dto.topic.GetTopicResponseDto;
import com.openclassrooms.mdd_api.exception.ApiException;
import com.openclassrooms.mdd_api.mapper.TopicMapper;
import com.openclassrooms.mdd_api.model.Topic;
import com.openclassrooms.mdd_api.model.User;
import com.openclassrooms.mdd_api.repository.TopicRepository;
import com.openclassrooms.mdd_api.repository.UserRepository;
import com.openclassrooms.mdd_api.service.interfaces.ITopicService;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class TopicService implements ITopicService {

    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final TopicMapper topicMapper;

    private TopicService(final TopicRepository topicRepository, final UserRepository userRepository, final TopicMapper topicMapper) {
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
        this.topicMapper = topicMapper;
    }

    @Override
    public List<GetTopicResponseDto> getAllTopics() {
        List<Topic> topics = topicRepository.findAll();
        topics.sort(Comparator.comparing(Topic::getTitle));
        return topics.stream().map(this.topicMapper::toGetTopicResponseDto).toList();
    }

    @Override
    public List<GetTopicResponseDto> unsubscribeTopic(JwtAuthenticationToken token, int id) {
        int userId = Integer.parseInt(token.getToken().getSubject());
        Optional<User> user = this.userRepository.findById(userId);
        Optional<Topic> foundTopic = this.topicRepository.findById(id);
        if (foundTopic.isPresent() && user.isPresent()) {
            Topic currentTopic = foundTopic.get();
            currentTopic.removeUser(user.get());
            this.topicRepository.save(currentTopic);
            List<Topic> topicsList = user.get().getTopics();
            topicsList.sort(Comparator.comparing(Topic::getTitle));
            return topicsList.stream().map(this.topicMapper::toGetTopicResponseDto).toList();
        } else {
            throw new ApiException("Erreur lors du désabonnement !");
        }
    }

    @Override
    public List<GetTopicResponseDto> subscribeTopic(JwtAuthenticationToken token, int id) {
        int userId = Integer.parseInt(token.getToken().getSubject());
        Optional<User> user = this.userRepository.findById(userId);
        Optional<Topic> foundTopic = this.topicRepository.findById(id);
        if (foundTopic.isPresent() && user.isPresent()) {
            foundTopic.get().addUser(user.get());
            this.topicRepository.save(foundTopic.get());
            List<Topic> topicsList = user.get().getTopics();
            topicsList.sort(Comparator.comparing(Topic::getTitle));
            return topicsList.stream().map(this.topicMapper::toGetTopicResponseDto).toList();
        } else {
            throw new ApiException("Erreur lors de l'abonnement !");
        }

    }
}
