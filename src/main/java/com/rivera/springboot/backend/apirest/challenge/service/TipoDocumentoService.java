package com.rivera.springboot.backend.apirest.challenge.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rivera.springboot.backend.apirest.challenge.entity.TipoDocumento;
import com.rivera.springboot.backend.apirest.challenge.repository.TipoDocumentoRepository;

@Service
public class TipoDocumentoService {
	@Autowired
	private TipoDocumentoRepository tipoDocumentoRepository;

	public List<TipoDocumento> findAll() {
		return tipoDocumentoRepository.findAll();
	}
	
	public List<TipoDocumento> findAll(Boolean estado) {
		return tipoDocumentoRepository.findAllByEstado(estado);
	}

	public TipoDocumento findById(Long id) {
		return tipoDocumentoRepository.findById(id).orElse(null);
	}
	
	public TipoDocumento findByCodigo(String codigo) {
		return tipoDocumentoRepository.findByCodigo(codigo).orElse(null);
	}

	public TipoDocumento save(TipoDocumento tipoDocumento) {
		return tipoDocumentoRepository.save(tipoDocumento);
	}

	public void deleteById(Long id) {
		tipoDocumentoRepository.deleteById(id);
	}
}
