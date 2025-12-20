package com.openclassrooms.mddapi.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openclassrooms.mddapi.dto.topic.TopicCreateDto;
import com.openclassrooms.mddapi.dto.topic.TopicResponseDto;
import com.openclassrooms.mddapi.dto.topic.TopicUpdateDto;
import com.openclassrooms.mddapi.exceptions.ResourceNotFoundException;
import com.openclassrooms.mddapi.models.TopicEntity;
import com.openclassrooms.mddapi.repositorys.TopicRepository;
import com.openclassrooms.mddapi.services.mappers.TopicMapper;

@Service
@Transactional
public class TopicService {

    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;

    public TopicService(TopicRepository topicRepository, TopicMapper topicMapper) {
        this.topicRepository = topicRepository;
        this.topicMapper = topicMapper;
    }

    public List<TopicResponseDto> findAll() {
        return topicRepository.findAll()
                .stream()
                .map(topicMapper::toDto)
                .collect(Collectors.toList());
    }

    public TopicResponseDto findById(Long id) {
        TopicEntity entity = topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id " + id));
        return topicMapper.toDto(entity);
    }

    public TopicResponseDto create(TopicCreateDto dto) {
        TopicEntity entity = topicMapper.fromCreateDto(dto);
        TopicEntity saved = topicRepository.save(entity);
        return topicMapper.toDto(saved);
    }

    public TopicResponseDto update(Long id, TopicUpdateDto dto) {
        TopicEntity existing = topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id " + id));

        // apply updates
        existing.setTitle(dto.title());
        existing.setDescription(dto.description());

        TopicEntity saved = topicRepository.save(existing);
        return topicMapper.toDto(saved);
    }

    public void delete(Long id) {
        if (!topicRepository.existsById(id)) {
            throw new ResourceNotFoundException("Topic not found with id " + id);
        }
        topicRepository.deleteById(id);
    }
}
