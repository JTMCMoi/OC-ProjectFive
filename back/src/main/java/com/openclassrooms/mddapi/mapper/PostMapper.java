package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.CommentResponse;
import com.openclassrooms.mddapi.dto.PostResponse;
import com.openclassrooms.mddapi.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper responsible for converting Post entities to PostResponse DTOs.
 */
@Component
@RequiredArgsConstructor
public class PostMapper {


    public PostResponse toResponse(Post post) {
        return toResponse(post, new ArrayList<>());
    }


    public PostResponse toResponse(Post post, List<CommentResponse> comments) {
        if (post == null) {
            return null;
        }
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorUsername(post.getAuthor().getUsername())
                .themeTitle(post.getTheme().getTitle())
                .themeId(post.getTheme().getId())
                .createdAt(post.getCreatedAt())
                .comments(comments)
                .build();
    }
}

