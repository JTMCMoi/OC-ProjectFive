package com.openclassrooms.DTO.Response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserResponseDTO {
    private Integer id;
    private String username;
    private String email;
    private LocalDateTime createdAt;
}
