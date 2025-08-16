package com.openclassrooms.mddapi.service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.dto.CurrentUserInfo;
import com.openclassrooms.mddapi.dto.LoginRequest;
import com.openclassrooms.mddapi.dto.RegisterOrUpdateRequest;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	@Lazy
	private final AuthenticationManager authManager;

	private final UserRepository userRepo;
	private final PasswordEncoder encoder;
	private final JwtUtil jwt;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User foundUser = userRepo.findByEmailOrUsername(username, username)
				.orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + username));

		return new org.springframework.security.core.userdetails.User(foundUser.getUsername(), foundUser.getPassword(),
				Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
	}

	@Override
	public ResponseEntity<?> register(RegisterOrUpdateRequest request) {
		if (userRepo.existsByEmailOrUsername(request.getEmail(), request.getUsername())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("E-mail ou pseudo déjà utilisé");
		}

		try {
			User newUser = User.builder().email(request.getEmail()).username(request.getUsername())
					.password(encoder.encode(request.getPassword())).build();

			userRepo.save(newUser);

			String accessToken = generateAccessTokenFromUsername(newUser.getUsername());

			ResponseCookie accessCookie = ResponseCookie.from("ACCESS_TOKEN", accessToken).httpOnly(true).secure(true)
					.sameSite("Lax").path("/").maxAge(jwt.getExpirationTime() / 1000).build();

			return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, accessCookie.toString())
					.body(Map.of("expiresIn", jwt.getExpirationTime() / 1000));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erreur lors de la création du compte : " + e.getMessage());
		}
	}

	@Override
	public ResponseEntity<?> login(LoginRequest request) {
		try {
			Authentication authentication = authManager
					.authenticate(new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));

			String accessToken = generateAccessTokenFromUsername(request.getLogin());

			ResponseCookie accessCookie = ResponseCookie.from("ACCESS_TOKEN", accessToken).httpOnly(true).secure(true)
					.sameSite("Lax").path("/").maxAge(jwt.getExpirationTime() / 1000).build();

			return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, accessCookie.toString())
					.body(Map.of("expiresIn", jwt.getExpirationTime() / 1000));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Identifiants invalides");
		}
	}

	private String generateAccessTokenFromUsername(String username) throws UserPrincipalNotFoundException {
		User user = userRepo.findByEmailOrUsername(username, username)
				.orElseThrow(() -> new UserPrincipalNotFoundException("Utilisateur non trouvé : " + username));

		String sessionId = UUID.randomUUID().toString();
		return jwt.generateToken(user.getUsername(), sessionId);
	}

	@Override
	public ResponseEntity<?> getCurrentUserInfo(String username) {
		try {
			User currentUser = userRepo.findByEmailOrUsername(username, username)
					.orElseThrow(() -> new UserPrincipalNotFoundException("Utilisateur non trouvé : " + username));

			CurrentUserInfo userInfo = CurrentUserInfo.builder().email(currentUser.getEmail())
					.username(currentUser.getUsername()).role("ROLE_USER").build();

			return ResponseEntity.ok(userInfo);

		} catch (UserPrincipalNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
		}
	}

	@Override
	public ResponseEntity<?> updateUser(RegisterOrUpdateRequest request) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String username = auth.getName();

			Optional<User> optionalUser = userRepo.findByEmailOrUsername(username, username);

			if (!optionalUser.isPresent()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
			}

			User user = optionalUser.get();

			user.setEmail(request.getEmail());
			user.setUsername(request.getUsername());

			if (request.getPassword() != null && !request.getPassword().isEmpty()) {
				user.setPassword(encoder.encode(request.getPassword()));
			}

			userRepo.save(user);

			String accessToken = generateAccessTokenFromUsername(user.getUsername());

			ResponseCookie accessCookie = ResponseCookie.from("ACCESS_TOKEN", accessToken).httpOnly(true).secure(true)
					.sameSite("Lax").path("/").maxAge(jwt.getExpirationTime() / 1000).build();

			return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, accessCookie.toString())
					.body(Map.of("expiresIn", jwt.getExpirationTime() / 1000));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erreur lors de la mise à jour : " + e.getMessage());
		}
	}
}
