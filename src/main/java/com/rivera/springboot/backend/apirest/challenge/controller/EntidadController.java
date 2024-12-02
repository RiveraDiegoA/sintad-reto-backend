package com.rivera.springboot.backend.apirest.challenge.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.rivera.springboot.backend.apirest.challenge.dto.EntidadDTO;
import com.rivera.springboot.backend.apirest.challenge.entity.Entidad;
import com.rivera.springboot.backend.apirest.challenge.entity.TipoContribuyente;
import com.rivera.springboot.backend.apirest.challenge.entity.TipoDocumento;
import com.rivera.springboot.backend.apirest.challenge.exception.ArgumentNotValidException;
import com.rivera.springboot.backend.apirest.challenge.exception.DuplicateRecordOrBadRequestException;
import com.rivera.springboot.backend.apirest.challenge.exception.ResourceNotFoundException;
import com.rivera.springboot.backend.apirest.challenge.service.EntidadService;
import com.rivera.springboot.backend.apirest.challenge.service.TipoContribuyenteService;
import com.rivera.springboot.backend.apirest.challenge.service.TipoDocumentoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/entidad")
@Tag(name = "Entidad", description = "APIs para gestionar las entidades. Requieren autenticación mediante token JWT.")
@SecurityRequirement(name = "bearerAuth")
public class EntidadController {

	@Autowired
	private EntidadService entidadService;

	@Autowired
	private TipoDocumentoService tipoDocumentoService;

	@Autowired
	private TipoContribuyenteService tipoContribuyenteService;

	@Operation(summary = "Obtener todas las entidades", description = "Devuelve una lista de todas las entidades registradas.", responses = {
			@ApiResponse(responseCode = "200", description = "Lista obtenida con éxito."),
			@ApiResponse(responseCode = "401", description = "No autorizado. Token JWT ausente o inválido.", content = @Content),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content) })
	@GetMapping
	public ResponseEntity<?> findAll() {
		List<Entidad> entidades = entidadService.findAll();
		Map<String, Object> response = new HashMap<>();

		response.put("message", "Registros encontrados!");
		response.put("data", entidades);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(summary = "Obtener entidades por estado", description = "Devuelve una lista de entidades filtradas por estado (activo/inactivo).", responses = {
			@ApiResponse(responseCode = "200", description = "Lista filtrada obtenida con éxito."),
			@ApiResponse(responseCode = "401", description = "No autorizado. Token JWT ausente o inválido.", content = @Content),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content) })
	@GetMapping("/estado/{estado}")
	public ResponseEntity<?> findAll(@PathVariable Boolean estado) {
		List<Entidad> entidades = entidadService.findAll(estado);
		Map<String, Object> response = new HashMap<>();

		response.put("message", "Registros encontrados!");
		response.put("data", entidades);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(summary = "Obtener una entidad por ID", description = "Devuelve la información de una entidad especificada por su ID.", responses = {
			@ApiResponse(responseCode = "200", description = "Entidad encontrada con éxito."),
			@ApiResponse(responseCode = "404", description = "No se encontró la entidad con el ID especificado.", content = @Content),
			@ApiResponse(responseCode = "401", description = "No autorizado. Token JWT ausente o inválido.", content = @Content) })
	@GetMapping("/{entidadId}")
	public ResponseEntity<?> findById(@PathVariable Long entidadId) {
		Entidad entidadEncontrada = entidadService.findById(entidadId);

		if (entidadEncontrada == null) {
			throw new ResourceNotFoundException(
					"La entidad con el ID (".concat(entidadId.toString()).concat(") no existe."));
		}

		Map<String, Object> response = new HashMap<>();
		response.put("message", "Registro encontrado!");
		response.put("data", entidadEncontrada);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(summary = "Crear una nueva entidad", description = "Registra una nueva entidad con los datos proporcionados.", responses = {
			@ApiResponse(responseCode = "201", description = "Entidad creada con éxito."),
			@ApiResponse(responseCode = "400", description = "Datos inválidos o entidad duplicada.", content = @Content),
			@ApiResponse(responseCode = "401", description = "No autorizado. Token JWT ausente o inválido.", content = @Content) })
	@PostMapping
	public ResponseEntity<?> save(@Valid @RequestBody EntidadDTO entidad, BindingResult result) {
		if (result.hasErrors()) {
			throw new ArgumentNotValidException(result);
		}

		Entidad entidadDuplicada = entidadService.findByNroDocumento(entidad.getNroDocumento());

		if (entidadDuplicada != null) {
			throw new DuplicateRecordOrBadRequestException(
					"La entidad con el nro. de documento (".concat(entidad.getNroDocumento()).concat(") ya existe."));
		}

		Entidad nuevaEntidad = new Entidad();
		nuevaEntidad.setNroDocumento(entidad.getNroDocumento());
		nuevaEntidad.setRazonSocial(entidad.getRazonSocial());
		nuevaEntidad.setNombreComercial(entidad.getNombreComercial());
		nuevaEntidad.setDireccion(entidad.getDireccion());
		nuevaEntidad.setTelefono(entidad.getTelefono());
		nuevaEntidad.setEstado(entidad.getEstado() != null ? entidad.getEstado() : true);

		if (entidad.getTipoDocumentoId() != null) {
			TipoDocumento tipoDocumento = tipoDocumentoService.findById(entidad.getTipoDocumentoId());
			if (tipoDocumento == null) {
				throw new ResourceNotFoundException("El tipo de documento con el ID ("
						.concat(entidad.getTipoDocumentoId().toString()).concat(") no existe."));
			}
			nuevaEntidad.setTipoDocumento(tipoDocumento);
		}

		if (entidad.getTipoContribuyenteId() != null) {
			TipoContribuyente tipoContribuyente = tipoContribuyenteService.findById(entidad.getTipoContribuyenteId());
			if (tipoContribuyente == null) {
				throw new ResourceNotFoundException("El tipo de contribuyente con el ID ("
						.concat(entidad.getTipoContribuyenteId().toString()).concat(") no existe."));
			}
			nuevaEntidad.setTipoContribuyente(tipoContribuyente);
		}

		entidadService.save(nuevaEntidad);

		Map<String, Object> response = new HashMap<>();
		response.put("message", "La entidad ha sido creada con éxito!");
		response.put("data", nuevaEntidad);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@Operation(summary = "Actualizar una entidad", description = "Actualiza la información de una entidad existente.", responses = {
			@ApiResponse(responseCode = "200", description = "Entidad actualizada con éxito."),
			@ApiResponse(responseCode = "404", description = "No se encontró la entidad con el ID especificado.", content = @Content),
			@ApiResponse(responseCode = "401", description = "No autorizado. Token JWT ausente o inválido.", content = @Content) })
	@PutMapping("/{entidadId}")
	public ResponseEntity<?> update(@PathVariable Long entidadId, @Valid @RequestBody EntidadDTO entidad,
			BindingResult result) {
		if (result.hasErrors()) {
			throw new ArgumentNotValidException(result);
		}

		Entidad entidadEncontrada = entidadService.findById(entidadId);

		if (entidadEncontrada == null) {
			throw new ResourceNotFoundException(
					"La entidad con el ID (".concat(entidadId.toString()).concat(") no existe."));
		}

		Entidad entidadDuplicada = entidadService.findByNroDocumento(entidad.getNroDocumento());

		if (entidadDuplicada != null && !entidadEncontrada.getId().equals(entidadDuplicada.getId())) {
			throw new DuplicateRecordOrBadRequestException(
					"La entidad con el nro. de documento (".concat(entidad.getNroDocumento()).concat(") ya existe."));
		}

		entidadEncontrada.setNroDocumento(entidad.getNroDocumento());
		entidadEncontrada.setRazonSocial(entidad.getRazonSocial());
		entidadEncontrada.setNombreComercial(entidad.getNombreComercial());
		entidadEncontrada.setDireccion(entidad.getDireccion());
		entidadEncontrada.setTelefono(entidad.getTelefono());
		entidadEncontrada.setEstado(entidad.getEstado() != null ? entidad.getEstado() : entidadEncontrada.getEstado());

		if (entidad.getTipoDocumentoId() != null) {
			TipoDocumento tipoDocumento = tipoDocumentoService.findById(entidad.getTipoDocumentoId());
			if (tipoDocumento == null) {
				throw new ResourceNotFoundException("El tipo de documento con el ID ("
						.concat(entidad.getTipoDocumentoId().toString()).concat(") no existe."));
			}
			entidadEncontrada.setTipoDocumento(tipoDocumento);
		}

		if (entidad.getTipoContribuyenteId() != null) {
			TipoContribuyente tipoContribuyente = tipoContribuyenteService.findById(entidad.getTipoContribuyenteId());
			if (tipoContribuyente == null) {
				throw new ResourceNotFoundException("El tipo de contribuyente con el ID ("
						.concat(entidad.getTipoContribuyenteId().toString()).concat(") no existe."));
			}
			entidadEncontrada.setTipoContribuyente(tipoContribuyente);
		}

		entidadService.save(entidadEncontrada);

		Map<String, Object> response = new HashMap<>();
		response.put("message", "La entidad ha sido actualizada con éxito!");
		response.put("data", entidadEncontrada);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(summary = "Eliminar una entidad", description = "Elimina una entidad por su ID.", responses = {
			@ApiResponse(responseCode = "200", description = "Entidad eliminada con éxito."),
			@ApiResponse(responseCode = "404", description = "No se encontró la entidad con el ID especificado.", content = @Content),
			@ApiResponse(responseCode = "401", description = "No autorizado. Token JWT ausente o inválido.", content = @Content) })
	@DeleteMapping("/{entidadId}")
	public ResponseEntity<?> deleteById(@PathVariable Long entidadId) {
		Entidad entidadEncontrada = entidadService.findById(entidadId);

		if (entidadEncontrada == null) {
			throw new ResourceNotFoundException(
					"La entidad con el ID (".concat(entidadId.toString()).concat(") no existe."));
		}

		entidadService.deleteById(entidadId);

		Map<String, Object> response = new HashMap<>();
		response.put("message", "La entidad ha sido eliminada con éxito!");

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
