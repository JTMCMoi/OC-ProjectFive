package com.openclassrooms.Services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.openclassrooms.DTO.Request.CreateCommentRequestDTO;
import com.openclassrooms.DTO.Request.CreatePostRequestDTO;
import com.openclassrooms.DTO.Response.CommentResponseDTO;
import com.openclassrooms.DTO.Response.PostDetailResponseDTO;
import com.openclassrooms.DTO.Response.PostResponseDTO;
import com.openclassrooms.Models.CommentEntity;
import com.openclassrooms.Models.PostEntity;
import com.openclassrooms.Models.SubscriptionEntity;
import com.openclassrooms.Models.TopicEntity;
import com.openclassrooms.Models.UserEntity;
import com.openclassrooms.Repositories.CommentRepository;
import com.openclassrooms.Repositories.PostRepository;
import com.openclassrooms.Repositories.SubscriptionRepository;
import com.openclassrooms.Repositories.TopicRepository;
import com.openclassrooms.Repositories.UserRepository;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private CommentRepository commentRepository;

    private UserEntity getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                                            .getAuthentication()
                                            .getName();
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
    }

    private PostResponseDTO toDTO(PostEntity post) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setAuthorUsername(post.getAuthor().getUsername());
        dto.setTopicName(post.getTopic().getName());
        dto.setCreatedAt(post.getCreatedAt());
        return dto;
    }

    public List<PostResponseDTO> getFeed(String sort) {
        UserEntity user = getCurrentUser();

        List<TopicEntity> subscribedTopics = subscriptionRepository
            .findByUser(user)
            .stream()
            .map(SubscriptionEntity::getTopic)
            .collect(Collectors.toList());

        if (subscribedTopics.isEmpty()) {
            return List.of();
        }

        List<PostEntity> posts;
        if ("asc".equalsIgnoreCase(sort)) {
            posts = postRepository.findByTopicInOrderByCreatedAtAsc(subscribedTopics);
        } else {
            posts = postRepository.findByTopicInOrderByCreatedAtDesc(subscribedTopics);
        }

        return posts.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public PostResponseDTO createPost(CreatePostRequestDTO request) {

        System.out.println("topicId reçu : " + request.getTopicId());
        System.out.println("title reçu : " + request.getTitle());
        System.out.println("content reçu : " + request.getContent());
        
        UserEntity author = getCurrentUser();

        TopicEntity topic = topicRepository.findById(request.getTopicId())
            .orElseThrow(() -> new RuntimeException("Thème introuvable"));
        
        PostEntity post = new PostEntity();
        post.setAuthor(author);
        post.setTopic(topic);
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        PostEntity saved = postRepository.save(post);
        return toDTO(saved);
    }

    public PostDetailResponseDTO getPostById(Integer id) {
        PostEntity post = postRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Article introuvable"));

        List<CommentResponseDTO> comments = commentRepository
            .findByPostOrderByCreatedAtAsc(post)
            .stream()
            .map(comment -> {
                CommentResponseDTO dto = new CommentResponseDTO();
                dto.setId(comment.getId());
                dto.setAuthorUsername(comment.getAuthor().getUsername());
                dto.setContent(comment.getContent());
                dto.setCreatedAt(comment.getCreatedAt());
                return dto;
            })
            .collect(Collectors.toList());

        PostDetailResponseDTO dto = new PostDetailResponseDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setAuthorUsername(post.getAuthor().getUsername());
        dto.setTopicName(post.getTopic().getName());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setComments(comments);

        return dto;
    }

    public CommentResponseDTO addComment(Integer postId, CreateCommentRequestDTO request) {
        UserEntity author = getCurrentUser();

        PostEntity post = postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("Article introuvable"));

        CommentEntity comment = new CommentEntity();
        comment.setAuthor(author);
        comment.setPost(post);
        comment.setContent(request.getContent());

        CommentEntity saved = commentRepository.save(comment);

        CommentResponseDTO dto = new CommentResponseDTO();
        dto.setId(saved.getId());
        dto.setAuthorUsername(saved.getAuthor().getUsername());
        dto.setContent(saved.getContent());
        dto.setCreatedAt(saved.getCreatedAt());

        return dto;
    }
}
