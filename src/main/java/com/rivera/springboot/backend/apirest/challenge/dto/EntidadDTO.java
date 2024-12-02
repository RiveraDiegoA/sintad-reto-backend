package com.rivera.springboot.backend.apirest.challenge.dto;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EntidadDTO {
	@NotEmpty
	@Size(min = 8, max = 20)
	private String nroDocumento;
	
	@NotEmpty
	@Size(min = 2, max = 200)
	private String razonSocial;
	
	@Size(max = 200)
	private String nombreComercial;
	
	@Size(max = 200)
	private String direccion;
	
	@Size(max = 50)
	private String telefono;
	
	private Boolean estado;
	
	@NotNull
	@Transient
	private Long tipoDocumentoId;
	
	@NotNull
	@Transient
	private Long tipoContribuyenteId;
}
