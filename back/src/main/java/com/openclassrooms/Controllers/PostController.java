package com.openclassrooms.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.DTO.Request.CreateCommentRequestDTO;
import com.openclassrooms.DTO.Request.CreatePostRequestDTO;
import com.openclassrooms.DTO.Response.CommentResponseDTO;
import com.openclassrooms.DTO.Response.PostDetailResponseDTO;
import com.openclassrooms.DTO.Response.PostResponseDTO;
import com.openclassrooms.Models.CommentEntity;
import com.openclassrooms.Services.PostService;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/feed")
    public ResponseEntity<List<PostResponseDTO>> getFeed(
        @RequestParam(defaultValue = "desc") String sort) {
        return ResponseEntity.ok(postService.getFeed(sort));
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@RequestBody CreatePostRequestDTO request) {
        return ResponseEntity.ok(postService.createPost(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDetailResponseDTO> getPostById(@PathVariable Integer id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentResponseDTO> addComment(@PathVariable Integer id, @RequestBody CreateCommentRequestDTO request) {
        return ResponseEntity.ok(postService.addComment(id, request));
    }   
}
