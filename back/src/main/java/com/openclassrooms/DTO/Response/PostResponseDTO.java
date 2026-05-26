package com.openclassrooms.DTO.Response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PostResponseDTO {
    private Integer id;
    private String title;
    private String content;
    private String authorUsername;
    private String topicName;
    private LocalDateTime createdAt;
}
