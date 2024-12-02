package com.rivera.springboot.backend.apirest.challenge.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_entidad")
public class Entidad implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_entidad")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_tipo_documento", nullable = false)
	private TipoDocumento tipoDocumento;

	@NotEmpty
	@Size(min = 8, max = 20)
	@Column(name = "nro_documento", length = 20, nullable = false, unique = true)
	private String nroDocumento;

	@NotEmpty
	@Size(min = 2, max = 200)
	@Column(name = "razon_social", length = 200, nullable = false)
	private String razonSocial;

	@Size(max = 200)
	@Column(name = "nombre_comercial", length = 200)
	private String nombreComercial;

	@ManyToOne
	@JoinColumn(name = "id_tipo_contribuyente", nullable = false)
	private TipoContribuyente tipoContribuyente;

	@Size(max = 200)
	@Column(name = "direccion", length = 200)
	private String direccion;

	@Size(max = 50)
	@Column(name = "telefono", length = 50)
	private String telefono;

	@Column(name = "estado", nullable = false)
	private Boolean estado;

	@PrePersist
	private void prePersist() {
		if (this.estado == null) {
			this.estado = true;
		}
	}
}
