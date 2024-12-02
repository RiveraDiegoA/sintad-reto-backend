package com.rivera.springboot.backend.apirest.challenge.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_tipo_documento")
public class TipoDocumento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_tipo_documento")
	private Long id;

	@NotEmpty
	@Size(min = 2, max = 20)
	@Column(name = "codigo", length = 20, nullable = false, unique = true)
	private String codigo;

	@NotEmpty
	@Size(min = 2, max = 100)
	@Column(name = "nombre", length = 100, nullable = false, unique = true)
	private String nombre;

	@Size(max = 200)
	@Column(name = "descripcion", length = 200)
	private String descripcion;

	@Column(name = "estado", nullable = false)
	private Boolean estado;

	@PrePersist
	private void prePersist() {
		if (this.estado == null) {
			this.estado = true;
		}
	}
}
