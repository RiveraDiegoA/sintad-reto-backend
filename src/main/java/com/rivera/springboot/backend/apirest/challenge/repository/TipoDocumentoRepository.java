package com.rivera.springboot.backend.apirest.challenge.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rivera.springboot.backend.apirest.challenge.entity.TipoDocumento;

@Repository
public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, Long> {
	List<TipoDocumento> findAllByEstado(Boolean estado);

	Optional<TipoDocumento> findByCodigo(String codigo);
}
