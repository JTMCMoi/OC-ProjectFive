package com.openclassrooms.mddapi.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openclassrooms.mddapi.dto.post.*;
import com.openclassrooms.mddapi.exceptions.ResourceNotFoundException;
import com.openclassrooms.mddapi.models.PostEntity;
import com.openclassrooms.mddapi.models.TopicEntity;
import com.openclassrooms.mddapi.models.UserEntity;
import com.openclassrooms.mddapi.repositorys.PostRepository;
import com.openclassrooms.mddapi.repositorys.TopicRepository;
import com.openclassrooms.mddapi.repositorys.UserRepository;
import com.openclassrooms.mddapi.services.mappers.PostMapper;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final AuthenticationService auth;
    private final PostMapper mapper;

    public PostResponseDto create(PostCreateDto dto) {

        String identifier = auth.getAuthenticatedIdentifier();

        UserEntity author = userRepository.findByEmailOrUsername(identifier)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        TopicEntity topic = topicRepository.findById(dto.topicId())
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));

        PostEntity entity = mapper.fromCreateDto(dto);
        entity.setAuthor(author);
        entity.setTopic(topic);

        return mapper.toDto(postRepository.save(entity));
    }

    public List<PostResponseDto> getAll() {
        return postRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public PostResponseDto getById(Long id) {
        return mapper.toDto(postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found")));
    }

    public List<PostResponseDto> getByTopic(Long topicId) {
        return postRepository.findByTopicId(topicId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public void delete(Long id) {
        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("Post not found");
        }
        postRepository.deleteById(id);
    }
}
