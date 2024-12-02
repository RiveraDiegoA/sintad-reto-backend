package com.rivera.springboot.backend.apirest.challenge.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TipoContribuyenteDTO {
	@NotEmpty
	@Size(min = 2, max = 100)
	private String nombre;

	private Boolean estado;
}
