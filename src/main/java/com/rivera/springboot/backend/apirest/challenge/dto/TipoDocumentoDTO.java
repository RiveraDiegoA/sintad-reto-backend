package com.rivera.springboot.backend.apirest.challenge.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TipoDocumentoDTO {
	@NotEmpty
	@Size(min = 2, max = 20)
	private String codigo;

	@NotEmpty
	@Size(min = 2, max = 100)
	private String nombre;

	@Size(max = 200)
	private String descripcion;

	private Boolean estado;
}
