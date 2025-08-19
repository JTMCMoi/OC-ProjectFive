package com.openclassrooms.mdd_api.service.interfaces;

import com.openclassrooms.mdd_api.dto.post.CreatePostRequestDto;
import com.openclassrooms.mdd_api.dto.post.GetPostResponseDto;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;

public interface IPostService {
    List<GetPostResponseDto> getAllPostsByTopicIds(JwtAuthenticationToken token);

    GetPostResponseDto getPostById(int id);

    void createPost(JwtAuthenticationToken token, CreatePostRequestDto post);


}
