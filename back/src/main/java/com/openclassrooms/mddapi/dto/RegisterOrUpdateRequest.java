package com.openclassrooms.mddapi.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class RegisterOrUpdateRequest {
	private String email;
	private String username;
	private String password;
}
