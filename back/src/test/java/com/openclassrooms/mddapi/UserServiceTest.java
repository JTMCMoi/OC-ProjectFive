package com.openclassrooms.mddapi;

import com.openclassrooms.mddapi.dto.UpdateUserRequest;
import com.openclassrooms.mddapi.dto.UserResponse;
import com.openclassrooms.mddapi.mapper.UserMapper;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private UserMapper userMapper;
    @InjectMocks private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testuser");
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(userDetails);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        testUser = User.builder()
                .id(1L)
                .email("test@test.com")
                .username("testuser")
                .password("encodedPassword")
                .build();

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
    }

    // ─── getCurrentUser ──────────────────────────────────────────────────────────

    @Test
    void getCurrentUser_doitRetournerLeProfilDeLutilisateur() {
        UserResponse response = UserResponse.builder()
                .id(1L).email("test@test.com").username("testuser").build();
        when(userMapper.toResponse(testUser)).thenReturn(response);

        UserResponse result = userService.getCurrentUser();

        assertThat(result.getUsername()).isEqualTo("testuser");
        assertThat(result.getEmail()).isEqualTo("test@test.com");
    }

    // ─── updateCurrentUser ───────────────────────────────────────────────────────

    @Test
    void updateCurrentUser_doitMettreAJourEmailEtUsername() {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .email("nouveau@test.com")
                .username("nouveauuser")
                .build();

        when(userRepository.existsByEmail("nouveau@test.com")).thenReturn(false);
        when(userRepository.existsByUsername("nouveauuser")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        UserResponse response = UserResponse.builder()
                .id(1L).email("nouveau@test.com").username("nouveauuser").build();
        when(userMapper.toResponse(any(User.class))).thenReturn(response);

        UserResponse result = userService.updateCurrentUser(request);

        assertThat(result.getEmail()).isEqualTo("nouveau@test.com");
        verify(userRepository).save(testUser);
    }

    @Test
    void updateCurrentUser_doitLeverExceptionSiEmailDejaUtilise() {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .email("autre@test.com")
                .build();

        when(userRepository.existsByEmail("autre@test.com")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.updateCurrentUser(request));
        assertThat(ex.getMessage()).contains("Email already in use");
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateCurrentUser_doitLeverExceptionSiUsernameDejaUtilise() {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .username("autreuser")
                .build();

        when(userRepository.existsByUsername("autreuser")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.updateCurrentUser(request));
        assertThat(ex.getMessage()).contains("Username already in use");
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateCurrentUser_doitEncoderLeMotDePasse() {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .password("NewPassword1!")
                .build();

        when(passwordEncoder.encode("NewPassword1!")).thenReturn("newEncoded");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userMapper.toResponse(any(User.class))).thenReturn(
                UserResponse.builder().id(1L).username("testuser").email("test@test.com").build());

        userService.updateCurrentUser(request);

        verify(passwordEncoder).encode("NewPassword1!");
        assertThat(testUser.getPassword()).isEqualTo("newEncoded");
    }

    @Test
    void updateCurrentUser_doitIgnorerEmailIdentique() {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .email("test@test.com")
                .build();

        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userMapper.toResponse(any(User.class))).thenReturn(
                UserResponse.builder().id(1L).username("testuser").email("test@test.com").build());

        userService.updateCurrentUser(request);

        verify(userRepository, never()).existsByEmail(any());
    }

    @Test
    void updateCurrentUser_doitIgnorerUsernameIdentique() {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .username("testuser")
                .build();

        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userMapper.toResponse(any(User.class))).thenReturn(
                UserResponse.builder().id(1L).username("testuser").email("test@test.com").build());

        userService.updateCurrentUser(request);

        verify(userRepository, never()).existsByUsername(any());
    }
}
