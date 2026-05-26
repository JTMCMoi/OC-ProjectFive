package com.openclassrooms.DTO.Request;

import lombok.Data;

@Data
public class UpdateUserRequestDTO {
    private String username;
    private String email;
    private String password;
}
