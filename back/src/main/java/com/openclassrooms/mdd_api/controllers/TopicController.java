package com.openclassrooms.mdd_api.controllers;

import com.openclassrooms.mdd_api.dto.topic.GetTopicResponseDto;
import com.openclassrooms.mdd_api.exception.ApiException;
import com.openclassrooms.mdd_api.service.implementations.TopicService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/topics")
public class TopicController {
    private final TopicService topicService;

    TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllTopics() {
        return ResponseEntity.ok().body(this.topicService.getAllTopics());
    }

    @PutMapping("/subscribe/{id}")
    public ResponseEntity<?> subscribeTopic(final JwtAuthenticationToken token, @PathVariable final int id) {
        try {
            List<GetTopicResponseDto> userTopics = this.topicService.subscribeTopic(token, id);
            return ResponseEntity.ok().body(userTopics);
        } catch (ApiException e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PutMapping("/unsubscribe/{id}")
    public ResponseEntity<?> unsubscribeTopic(final JwtAuthenticationToken token, @PathVariable final int id) {
        try {
            List<GetTopicResponseDto> userTopics = this.topicService.unsubscribeTopic(token, id);
            return ResponseEntity.ok().body(userTopics);
        } catch (ApiException e) {
            return ResponseEntity.badRequest().body(e);
        }
    }
}
