package com.openclassrooms.mddapi.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class LoginRequest {
	private String login;
	private String password;
}
