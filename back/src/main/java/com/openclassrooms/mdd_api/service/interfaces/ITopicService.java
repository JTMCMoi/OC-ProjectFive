package com.openclassrooms.mdd_api.service.interfaces;

import com.openclassrooms.mdd_api.dto.topic.GetTopicResponseDto;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;

public interface ITopicService {
    List<GetTopicResponseDto> getAllTopics();

    List<GetTopicResponseDto> subscribeTopic(JwtAuthenticationToken token, int userId);

    List<GetTopicResponseDto> unsubscribeTopic(JwtAuthenticationToken token, int userId);
}
