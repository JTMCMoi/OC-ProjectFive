package com.openclassrooms.mddapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Theme;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

	@Query("SELECT a FROM Article a WHERE a.theme.id IN:themeIds ORDER BY a.createdAt DESC")
	List<Article> findFeed(List<Long> themeIds);

	@Query("SELECT a FROM Article a WHERE a.theme IN :themes ORDER BY a.createdAt DESC")
	List<Article> findByThemesOrderByDateDesc(@Param("themes") List<Theme> themes);

	@Query("SELECT a FROM Article a WHERE a.theme IN :themes ORDER BY a.createdAt ASC")
	List<Article> findByThemesOrderByDateAsc(@Param("themes") List<Theme> themes);
}