package com.rivera.springboot.backend.apirest.challenge.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rivera.springboot.backend.apirest.challenge.entity.Entidad;
import com.rivera.springboot.backend.apirest.challenge.repository.EntidadRepository;

@Service
public class EntidadService {
	@Autowired
	private EntidadRepository entidadRepository;

	public List<Entidad> findAll() {
		return entidadRepository.findAll();
	}

	public List<Entidad> findAll(Boolean estado) {
		return entidadRepository.findAllByEstado(estado);
	}

	public Entidad findById(Long id) {
		return entidadRepository.findById(id).orElse(null);
	}

	public Entidad findByNroDocumento(String nroDocumento) {
		return entidadRepository.findByNroDocumento(nroDocumento).orElse(null);
	}

	public Entidad save(Entidad Entidad) {
		return entidadRepository.save(Entidad);
	}

	public void deleteById(Long id) {
		entidadRepository.deleteById(id);
	}
}
