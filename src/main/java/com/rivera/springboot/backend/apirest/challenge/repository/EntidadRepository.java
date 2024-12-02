package com.rivera.springboot.backend.apirest.challenge.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rivera.springboot.backend.apirest.challenge.entity.Entidad;

@Repository
public interface EntidadRepository extends JpaRepository<Entidad, Long> {
	List<Entidad> findAllByEstado(Boolean estado);

	Optional<Entidad> findByNroDocumento(String nroDocumento);
}
