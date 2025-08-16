package com.openclassrooms.mddapi.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.openclassrooms.mddapi.dto.LoginRequest;
import com.openclassrooms.mddapi.dto.RegisterOrUpdateRequest;

public interface UserService extends UserDetailsService {

	UserDetails loadUserByUsername(String username);

	ResponseEntity<?> register(RegisterOrUpdateRequest request);

	ResponseEntity<?> login(LoginRequest request);

	ResponseEntity<?> getCurrentUserInfo(String username);

	ResponseEntity<?> updateUser(RegisterOrUpdateRequest request);

}
