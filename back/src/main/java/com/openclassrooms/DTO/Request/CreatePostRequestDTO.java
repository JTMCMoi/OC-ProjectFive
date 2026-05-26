package com.openclassrooms.DTO.Request;

import lombok.Data;

@Data
public class CreatePostRequestDTO {
    private Integer topicId;
    private String title;
    private String content;
}
