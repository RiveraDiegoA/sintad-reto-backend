package com.rivera.springboot.backend.apirest.challenge.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_usuario")
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Long id;

	@NotEmpty
	@Size(min = 4, max = 20)
	@Column(name = "nombre_usuario", length = 20, unique = true, nullable = false)
	private String nombreUsuario;

	@NotEmpty
	@Size(min = 4)
	@Column(nullable = false)
	private String contrasena;

	@NotEmpty
	@Column(nullable = false)
	private String rol;

	@Column(nullable = false)
	private Boolean estado;

	@PrePersist
	public void prePersist() {
		if (this.estado == null) {
			this.estado = true;
		}
	}
}
