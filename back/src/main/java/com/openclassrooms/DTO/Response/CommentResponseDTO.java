package com.openclassrooms.DTO.Response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CommentResponseDTO {
    private Integer id;
    private String authorUsername;
    private String content;
    private LocalDateTime createdAt;
}
