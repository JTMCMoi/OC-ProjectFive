package com.openclassrooms.mddapi.service;

import java.util.List;

import com.openclassrooms.mddapi.dto.ArticleCreateRequest;
import com.openclassrooms.mddapi.dto.ArticleResponse;
import com.openclassrooms.mddapi.dto.CommentResponse;
import com.openclassrooms.mddapi.model.Article;

public interface ArticleService {

	Article save(ArticleCreateRequest newArticle);

	ArticleResponse findArticleById(Long id);

	List<ArticleResponse> getUserFeed(String sortOrder);

	List<CommentResponse> getCommentsByArticleId(Long articleId);

	void addComment(Long articleId, String content);
}
