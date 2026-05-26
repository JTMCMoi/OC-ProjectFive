package com.openclassrooms.DTO.Response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class PostDetailResponseDTO {
    private Integer id;
    private String title;
    private String content;
    private String authorUsername;
    private String topicName;
    private LocalDateTime createdAt;
    private List<CommentResponseDTO> comments;
}
