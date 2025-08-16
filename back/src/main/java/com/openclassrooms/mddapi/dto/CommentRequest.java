package com.openclassrooms.mddapi.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class CommentRequest {
    @NotBlank
    private String content;
}