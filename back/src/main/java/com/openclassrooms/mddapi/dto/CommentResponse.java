package com.openclassrooms.mddapi.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CommentResponse {
	private Long id;
	private String content;
	private String authorUsername;
	private Instant createdAt;
}
