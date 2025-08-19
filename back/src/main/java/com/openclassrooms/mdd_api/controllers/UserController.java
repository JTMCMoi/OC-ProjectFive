package com.openclassrooms.mdd_api.controllers;

import com.openclassrooms.mdd_api.dto.user.GetUserResponseDto;
import com.openclassrooms.mdd_api.dto.user.UserUpdateRequestDto;
import com.openclassrooms.mdd_api.exception.ApiException;
import com.openclassrooms.mdd_api.service.implementations.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<?> updateUser(final JwtAuthenticationToken token, @RequestBody final UserUpdateRequestDto request) {
        try {
            GetUserResponseDto updatedUser = this.userService.updateUser(token, request);
            return ResponseEntity.ok().body(updatedUser);
        } catch (ApiException e) {
            return ResponseEntity.badRequest().body(e);
        }

    }
}
