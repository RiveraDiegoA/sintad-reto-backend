package com.rivera.springboot.backend.apirest.challenge.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthDTO {
	@NotEmpty
	@Size(min = 4, max = 20)
	private String username;

	@NotEmpty
	@Size(min = 4, max = 20)
	private String password;
}
