package com.rivera.springboot.backend.apirest.challenge.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rivera.springboot.backend.apirest.challenge.entity.TipoContribuyente;
import com.rivera.springboot.backend.apirest.challenge.repository.TipoContribuyenteRepository;

@Service
public class TipoContribuyenteService {
	@Autowired
	private TipoContribuyenteRepository tipoContribuyenteRepository;

	public List<TipoContribuyente> findAll() {
		return tipoContribuyenteRepository.findAll();
	}

	public List<TipoContribuyente> findAll(Boolean estado) {
		return tipoContribuyenteRepository.findAllByEstado(estado);
	}

	public TipoContribuyente findById(Long id) {
		return tipoContribuyenteRepository.findById(id).orElse(null);
	}

	public TipoContribuyente findByNombre(String nombre) {
		return tipoContribuyenteRepository.findByNombre(nombre).orElse(null);
	}

	public TipoContribuyente save(TipoContribuyente TipoContribuyente) {
		return tipoContribuyenteRepository.save(TipoContribuyente);
	}

	public void deleteById(Long id) {
		tipoContribuyenteRepository.deleteById(id);
	}
}
