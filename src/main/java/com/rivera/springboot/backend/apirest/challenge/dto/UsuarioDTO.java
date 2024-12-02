package com.rivera.springboot.backend.apirest.challenge.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioDTO {
	@NotEmpty
	@Size(min = 4, max = 20)
	private String nombreUsuario;

	@NotEmpty
	@Size(min = 4, max = 20)
	private String contrasena;

	@NotEmpty
	private String rol;

	private Boolean estado;
}
