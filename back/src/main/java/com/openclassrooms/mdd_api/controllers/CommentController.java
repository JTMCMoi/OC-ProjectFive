package com.openclassrooms.mdd_api.controllers;

import com.openclassrooms.mdd_api.dto.comment.CreateCommentRequest;
import com.openclassrooms.mdd_api.exception.ApiException;
import com.openclassrooms.mdd_api.service.implementations.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/comments")
public class CommentController {

    private final CommentService commentService;

    CommentController(final CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<?> addComment(final JwtAuthenticationToken token, @RequestBody final CreateCommentRequest request) {
        try {
            return ResponseEntity.ok().body(this.commentService.addPostComment(token, request));
        } catch (ApiException e) {
            return ResponseEntity.badRequest().body(e);
        }
    }
}
