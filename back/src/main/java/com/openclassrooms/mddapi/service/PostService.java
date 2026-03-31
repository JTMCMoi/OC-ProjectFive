package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.CommentRequest;
import com.openclassrooms.mddapi.dto.CommentResponse;
import com.openclassrooms.mddapi.dto.PostRequest;
import com.openclassrooms.mddapi.dto.PostResponse;
import com.openclassrooms.mddapi.exception.NotSubscribedException;
import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
import com.openclassrooms.mddapi.mapper.CommentMapper;
import com.openclassrooms.mddapi.mapper.PostMapper;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final ThemeRepository themeRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;

    @Transactional(readOnly = true)
    public List<PostResponse> getFeed() {
        User currentUser = getCurrentUserEntity();
        Set<Theme> subscribedThemes = currentUser.getSubscribedThemes();
        if (subscribedThemes.isEmpty()) {
            return List.of();
        }
        return postRepository.findByThemeInOrderByCreatedAtDesc(subscribedThemes)
                .stream()
                .map(postMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponse createPost(PostRequest request) {
        User currentUser = getCurrentUserEntity();
        Theme theme = themeRepository.findById(request.getThemeId())
                .orElseThrow(() -> new ResourceNotFoundException("Thème non trouvé avec l'id : " + request.getThemeId()));

        boolean isSubscribed = currentUser.getSubscribedThemes()
                .stream()
                .anyMatch(t -> t.getId().equals(theme.getId()));
        if (!isSubscribed) {
            throw new NotSubscribedException(
                "Vous devez être abonné au thème '" + theme.getTitle() + "' pour y publier un article."
            );
        }

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(currentUser)
                .theme(theme)
                .build();

        Post saved = postRepository.save(post);
        log.info("Article créé par {} dans le thème {}", currentUser.getUsername(), theme.getTitle());
        return postMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article non trouvé avec l'id : " + id));

        List<CommentResponse> comments = commentRepository.findByPostIdOrderByCreatedAtAsc(id)
                .stream()
                .map(commentMapper::toResponse)
                .collect(Collectors.toList());

        PostResponse response = postMapper.toResponse(post, comments);
        return response;
    }

    @Transactional
    public CommentResponse addComment(Long postId, CommentRequest request) {
        User currentUser = getCurrentUserEntity();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Article non trouvé avec l'id : " + postId));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .author(currentUser)
                .post(post)
                .build();

        Comment saved = commentRepository.save(comment);
        log.info("Commentaire ajouté par {} sur l'article {}", currentUser.getUsername(), postId);
        return commentMapper.toResponse(saved);
    }

    private User getCurrentUserEntity() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
}

