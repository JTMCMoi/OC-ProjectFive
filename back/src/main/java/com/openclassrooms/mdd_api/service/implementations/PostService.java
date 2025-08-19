package com.openclassrooms.mdd_api.service.implementations;

import com.openclassrooms.mdd_api.dto.post.CreatePostRequestDto;
import com.openclassrooms.mdd_api.dto.post.GetPostResponseDto;
import com.openclassrooms.mdd_api.exception.ApiException;
import com.openclassrooms.mdd_api.mapper.PostMapper;
import com.openclassrooms.mdd_api.model.Post;
import com.openclassrooms.mdd_api.model.Topic;
import com.openclassrooms.mdd_api.model.User;
import com.openclassrooms.mdd_api.repository.PostRepository;
import com.openclassrooms.mdd_api.repository.TopicRepository;
import com.openclassrooms.mdd_api.repository.UserRepository;
import com.openclassrooms.mdd_api.service.interfaces.IPostService;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService implements IPostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final PostMapper postMapper;

    private PostService(final PostRepository postRepository, final UserRepository userRepository, final TopicRepository topicRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
        this.postMapper = postMapper;
    }

    @Override
    public List<GetPostResponseDto> getAllPostsByTopicIds(JwtAuthenticationToken token) {
        int userId = Integer.parseInt(token.getToken().getSubject());
        Optional<User> user = this.userRepository.findById(userId);
        if (user.isPresent()) {
            List<Integer> subscribedTopicsIds = user.get().getTopics().stream().map(Topic::getTopicId).toList();
            List<Post> postsList = this.postRepository.findByTopic_TopicIdIn(subscribedTopicsIds);
            return postsList.stream().map(postMapper::toGetPostResponseDto).toList();
        } else {
            throw new ApiException("Utitlisateur non trouvé");
        }

    }

    @Override
    public void createPost(JwtAuthenticationToken token, CreatePostRequestDto post) {
        int userId = Integer.parseInt(token.getToken().getSubject());
        Optional<User> user = this.userRepository.findById(userId);
        Optional<Topic> topic = this.topicRepository.findById(Integer.parseInt(post.topicId()));

        if (user.isEmpty() || topic.isEmpty()) {
            throw new ApiException("Utilisateur/Thème non trouvé");
        } else {
            Post newPost = this.postMapper.toEntity(post, user.get(), topic.get());
            this.postRepository.save(newPost);
        }
    }

    @Override
    public GetPostResponseDto getPostById(int id) {
        Optional<Post> foundPost = this.postRepository.findById(id);
        if (foundPost.isPresent()) {
            return postMapper.toGetPostResponseDto(foundPost.get());
        } else {
            throw new ApiException("Post non trouvé");
        }
    }
}
