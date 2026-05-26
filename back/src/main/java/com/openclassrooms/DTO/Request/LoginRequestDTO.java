package com.openclassrooms.DTO.Request;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String identifier; /* Email ou nom d'utilisateur */
    private String password;
}
