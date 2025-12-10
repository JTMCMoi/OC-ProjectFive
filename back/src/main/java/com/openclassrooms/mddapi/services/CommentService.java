package com.openclassrooms.mddapi.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openclassrooms.mddapi.dto.comment.*;
import com.openclassrooms.mddapi.exceptions.ResourceNotFoundException;
import com.openclassrooms.mddapi.models.CommentEntity;
import com.openclassrooms.mddapi.models.PostEntity;
import com.openclassrooms.mddapi.models.UserEntity;
import com.openclassrooms.mddapi.repositorys.CommentRepository;
import com.openclassrooms.mddapi.repositorys.PostRepository;
import com.openclassrooms.mddapi.repositorys.UserRepository;
import com.openclassrooms.mddapi.services.mappers.CommentMapper;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthenticationService auth;
    private final CommentMapper mapper;

    public CommentResponseDto create(CommentCreateDto dto) {

        String identifier = auth.getAuthenticatedIdentifier();

        UserEntity author = userRepository.findByEmailOrUsername(identifier)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        PostEntity post = postRepository.findById(dto.postId())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        CommentEntity entity = mapper.fromCreateDto(dto);
        entity.setAuthor(author);
        entity.setPost(post);

        return mapper.toDto(commentRepository.save(entity));
    }

    public List<CommentListItemDto> getByPost(Long postId) {
        return commentRepository.findByPostId(postId)
                .stream()
                .map(mapper::toListItem)
                .toList();
    }

    public void delete(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Comment not found");
        }
        commentRepository.deleteById(id);
    }
}
