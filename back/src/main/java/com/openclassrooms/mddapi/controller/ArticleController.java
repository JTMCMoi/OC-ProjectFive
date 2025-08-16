package com.openclassrooms.mddapi.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.dto.ArticleCreateRequest;
import com.openclassrooms.mddapi.dto.ArticleCreateResponse;
import com.openclassrooms.mddapi.dto.ArticleResponse;
import com.openclassrooms.mddapi.dto.CommentRequest;
import com.openclassrooms.mddapi.dto.CommentResponse;
import com.openclassrooms.mddapi.service.ArticleService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/article")
@RequiredArgsConstructor
@RestController
public class ArticleController {

	private final ArticleService articleService;

	@PostMapping
	public ResponseEntity<?> createArticle(@RequestBody ArticleCreateRequest request) {
		try {
			this.articleService.save(request);
			return ResponseEntity.ok(ArticleCreateResponse.builder().status(HttpStatus.CREATED)
					.message("Article créé avec succès").build());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getArticleDetails(@PathVariable Long id) {

		try {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.articleService.findArticleById(id));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	@GetMapping
	public ResponseEntity<List<ArticleResponse>> getFeed(@RequestParam(defaultValue = "desc") String sort) {
		List<ArticleResponse> feed = articleService.getUserFeed(sort);
		return ResponseEntity.ok(feed);
	}

	@GetMapping("/{articleId}/comments")
	public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long articleId) {
		List<CommentResponse> comments = articleService.getCommentsByArticleId(articleId);
		return ResponseEntity.ok(comments);
	}

	@PostMapping("/{articleId}/comments")
	public ResponseEntity<Void> addComment(@PathVariable Long articleId, @Valid @RequestBody CommentRequest request) {
		articleService.addComment(articleId, request.getContent());
		return ResponseEntity.ok().build();
	}

}
