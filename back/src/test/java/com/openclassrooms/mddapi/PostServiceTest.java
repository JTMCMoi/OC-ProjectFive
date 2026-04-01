package com.openclassrooms.mddapi;

import com.openclassrooms.mddapi.dto.*;
import com.openclassrooms.mddapi.exception.NotSubscribedException;
import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
import com.openclassrooms.mddapi.mapper.CommentMapper;
import com.openclassrooms.mddapi.mapper.PostMapper;
import com.openclassrooms.mddapi.model.*;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PostServiceTest {

    @Mock private PostRepository postRepository;
    @Mock private ThemeRepository themeRepository;
    @Mock private UserRepository userRepository;
    @Mock private CommentRepository commentRepository;
    @Mock private PostMapper postMapper;
    @Mock private CommentMapper commentMapper;
    @InjectMocks private PostService postService;

    private User testUser;
    private Theme testTheme;
    private Post testPost;

    @BeforeEach
    void setUp() {
        // Mock du contexte de sécurité Spring
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testuser");
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(userDetails);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        testUser = User.builder()
                .id(1L).username("testuser")
                .email("test@test.com").password("password")
                .subscribedThemes(new HashSet<>())
                .build();

        testTheme = Theme.builder()
                .id(1L).title("Java").description("Programmation Java")
                .build();

        testPost = Post.builder()
                .id(1L).title("Mon article")
                .content("Contenu").author(testUser).theme(testTheme)
                .build();

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
    }

    // ─── getFeed ─────────────────────────────────────────────────────────────────

    @Test
    void getFeed_doitRetournerListeVideSiAucunAbonnement() {
        List<PostResponse> result = postService.getFeed();
        assertThat(result).isEmpty();
        verify(postRepository, never()).findByThemeInOrderByCreatedAtDesc(any());
    }

    @Test
    void getFeed_doitRetournerLesArticlesDesThemesAbonnes() {
        testUser.getSubscribedThemes().add(testTheme);
        PostResponse postResponse = PostResponse.builder().id(1L).title("Mon article").build();
        when(postRepository.findByThemeInOrderByCreatedAtDesc(testUser.getSubscribedThemes()))
                .thenReturn(List.of(testPost));
        when(postMapper.toResponse(testPost)).thenReturn(postResponse);

        List<PostResponse> result = postService.getFeed();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Mon article");
    }

    // ─── getPost ─────────────────────────────────────────────────────────────────

    @Test
    void getPost_doitRetournerLarticleAvecSesCommentaires() {
        Comment comment = Comment.builder()
                .id(1L).content("Bravo !").author(testUser).post(testPost).build();
        CommentResponse commentResponse = CommentResponse.builder()
                .id(1L).content("Bravo !").authorUsername("testuser").build();
        PostResponse postResponse = PostResponse.builder()
                .id(1L).title("Mon article").comments(List.of(commentResponse)).build();

        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(commentRepository.findByPostIdOrderByCreatedAtAsc(1L)).thenReturn(List.of(comment));
        when(commentMapper.toResponse(comment)).thenReturn(commentResponse);
        when(postMapper.toResponse(testPost, List.of(commentResponse))).thenReturn(postResponse);

        PostResponse result = postService.getPost(1L);

        assertThat(result.getComments()).hasSize(1);
        assertThat(result.getComments().get(0).getContent()).isEqualTo("Bravo !");
    }

    @Test
    void getPost_doitLeverExceptionSiArticleInexistant() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> postService.getPost(99L));
    }

    // ─── createPost ──────────────────────────────────────────────────────────────

    @Test
    void createPost_doitLeverNotSubscribedExceptionSiNonAbonne() {
        // testUser n'est abonné à aucun thème par défaut
        PostRequest request = new PostRequest(1L, "Mon article", "Contenu");
        when(themeRepository.findById(1L)).thenReturn(Optional.of(testTheme));

        assertThrows(NotSubscribedException.class, () -> postService.createPost(request));
        verify(postRepository, never()).save(any());
    }

    @Test
    void createPost_doitSauvegarderEtRetournerLarticleSiAbonne() {
        testUser.getSubscribedThemes().add(testTheme); // l'utilisateur est abonné
        PostRequest request = new PostRequest(1L, "Mon article", "Contenu");
        PostResponse postResponse = PostResponse.builder().id(1L).title("Mon article").build();

        when(themeRepository.findById(1L)).thenReturn(Optional.of(testTheme));
        when(postRepository.save(any(Post.class))).thenReturn(testPost);
        when(postMapper.toResponse(testPost)).thenReturn(postResponse);

        PostResponse result = postService.createPost(request);

        assertThat(result.getTitle()).isEqualTo("Mon article");
        verify(postRepository).save(any(Post.class));
    }

    @Test
    void createPost_doitLeverExceptionSiThemeInexistant() {
        PostRequest request = new PostRequest(99L, "Titre", "Contenu");
        when(themeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> postService.createPost(request));
        verify(postRepository, never()).save(any());
    }

    // ─── addComment ──────────────────────────────────────────────────────────────

    @Test
    void addComment_doitSauvegarderEtRetournerLeCommentaire() {
        CommentRequest request = new CommentRequest("Bravo !");
        Comment savedComment = Comment.builder()
                .id(1L).content("Bravo !").author(testUser).post(testPost).build();
        CommentResponse commentResponse = CommentResponse.builder()
                .id(1L).content("Bravo !").authorUsername("testuser").build();

        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);
        when(commentMapper.toResponse(savedComment)).thenReturn(commentResponse);

        CommentResponse result = postService.addComment(1L, request);

        assertThat(result.getContent()).isEqualTo("Bravo !");
        assertThat(result.getAuthorUsername()).isEqualTo("testuser");
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void addComment_doitLeverExceptionSiArticleInexistant() {
        CommentRequest request = new CommentRequest("Commentaire");
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> postService.addComment(99L, request));
        verify(commentRepository, never()).save(any());
    }
}

