package com.openclassrooms.mddapi.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleResponse {
	private Long id;
	private String title;
	private String content;
	private String authorName;
	private Instant createdAt;
	private String theme;
}
