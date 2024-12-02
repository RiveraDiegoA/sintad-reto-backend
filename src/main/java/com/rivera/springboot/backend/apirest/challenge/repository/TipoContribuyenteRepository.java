package com.rivera.springboot.backend.apirest.challenge.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rivera.springboot.backend.apirest.challenge.entity.TipoContribuyente;

@Repository
public interface TipoContribuyenteRepository extends JpaRepository<TipoContribuyente, Long> {
	List<TipoContribuyente> findAllByEstado(Boolean estado);

	Optional<TipoContribuyente> findByNombre(String nombre);
}
