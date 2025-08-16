package com.openclassrooms.mddapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.dto.RegisterOrUpdateRequest;
import com.openclassrooms.mddapi.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PutMapping("")
	public ResponseEntity<?> updateUser(@RequestBody RegisterOrUpdateRequest updateRequest) {
		return userService.updateUser(updateRequest);
	}
}
