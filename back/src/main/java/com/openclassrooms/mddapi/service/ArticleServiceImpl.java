package com.openclassrooms.mddapi.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.dto.ArticleCreateRequest;
import com.openclassrooms.mddapi.dto.ArticleResponse;
import com.openclassrooms.mddapi.dto.CommentResponse;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.Subscription;
import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

	private final ArticleRepository articleRepo;
	private final UserRepository userRepo;
	private final ThemeRepository themRepo;
	private final SubscriptionRepository subscriptionRepo;
	private final CommentRepository commentRepository;

	@Override
	public Article save(ArticleCreateRequest newArticle) {
		User currentUser = this.getCurrentUser();

		Theme theme = this.themRepo.findById(newArticle.getTheme())
				.orElseThrow(() -> new RuntimeException("Theme non trouvé"));

		return this.articleRepo.save(Article.builder().author(currentUser).content(newArticle.getContent()).theme(theme)
				.title(newArticle.getTitle()).build());
	}

	@Override
	public ArticleResponse findArticleById(Long id) {
		Article foundedArticle = this.articleRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Article non trouvé"));
		return ArticleResponse.builder().authorName(foundedArticle.getAuthor().getUsername())
				.content(foundedArticle.getContent()).createdAt(foundedArticle.getCreatedAt())
				.id(foundedArticle.getId()).title(foundedArticle.getTitle()).theme(foundedArticle.getTheme().getName())
				.build();
	}

	@Override
	public List<ArticleResponse> getUserFeed(String sortOrder) {
		User currentUser = this.getCurrentUser();

		List<Theme> subscribedThemes = subscriptionRepo.findByUser(currentUser).stream().map(Subscription::getTheme)
				.collect(Collectors.toList());

		List<Article> articles;

		if ("asc".equalsIgnoreCase(sortOrder)) {
			articles = articleRepo.findByThemesOrderByDateAsc(subscribedThemes);
		} else {
			articles = articleRepo.findByThemesOrderByDateDesc(subscribedThemes);
		}

		return articles.stream()
				.map(article -> ArticleResponse.builder().id(article.getId()).title(article.getTitle())
						.content(article.getContent()).createdAt(article.getCreatedAt())
						.theme(article.getTheme().getName()).authorName(article.getAuthor().getUsername()).build())
				.collect(Collectors.toList());
	}

	@Override
	public List<CommentResponse> getCommentsByArticleId(Long articleId) {
		List<Comment> comments = commentRepository.findByArticleIdOrderByCreatedAtAsc(articleId);
		return comments.stream().map(c -> CommentResponse.builder().id(c.getId()).content(c.getContent())
				.authorUsername(c.getAuthor().getUsername()).createdAt(c.getCreatedAt()).build()).collect(Collectors.toList());
	}

	@Override
	public void addComment(Long articleId, String content) {
		User currentUser = this.getCurrentUser();
		Article article = articleRepo.findById(articleId).orElseThrow(() -> new RuntimeException("Article non trouvé"));

		Comment comment = Comment.builder().content(content).article(article).author(currentUser).build();
		commentRepository.save(comment);
	}

	private User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		return this.userRepo.findByEmailOrUsername(username, username)
				.orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
	}

}
