package com.openclassrooms.mdd_api.service.implementations;

import com.openclassrooms.mdd_api.dto.comment.CommentDto;
import com.openclassrooms.mdd_api.dto.comment.CreateCommentRequest;
import com.openclassrooms.mdd_api.exception.ApiException;
import com.openclassrooms.mdd_api.mapper.CommentMapper;
import com.openclassrooms.mdd_api.model.Comment;
import com.openclassrooms.mdd_api.model.Post;
import com.openclassrooms.mdd_api.model.User;
import com.openclassrooms.mdd_api.repository.PostRepository;
import com.openclassrooms.mdd_api.repository.UserRepository;
import com.openclassrooms.mdd_api.service.interfaces.ICommentService;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService implements ICommentService {

    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    private CommentService(final CommentMapper commentMapper, final UserRepository userRepository, final PostRepository postRepository) {
        this.commentMapper = commentMapper;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Override
    public List<CommentDto> addPostComment(final JwtAuthenticationToken token, final CreateCommentRequest request) {
        int userId = Integer.parseInt(token.getToken().getSubject());
        Optional<User> user = this.userRepository.findById(userId);
        Optional<Post> post = this.postRepository.findById(request.postId());
        if (user.isEmpty() || post.isEmpty()) {
            throw new ApiException("Erreur lors de la création du commentaire");
        } else {
            Comment newComment = new Comment();
            newComment.setUser(user.get());
            newComment.setContent(request.content());
            post.get().addComment(newComment);
            this.postRepository.save(post.get());
            return post.get().getComments().stream().map(this.commentMapper::toDto).toList();
        }
    }
}
