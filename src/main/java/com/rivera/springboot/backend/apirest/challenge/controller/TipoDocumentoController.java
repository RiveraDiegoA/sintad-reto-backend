package com.rivera.springboot.backend.apirest.challenge.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.rivera.springboot.backend.apirest.challenge.dto.TipoDocumentoDTO;
import com.rivera.springboot.backend.apirest.challenge.entity.TipoDocumento;
import com.rivera.springboot.backend.apirest.challenge.exception.ArgumentNotValidException;
import com.rivera.springboot.backend.apirest.challenge.exception.DuplicateRecordOrBadRequestException;
import com.rivera.springboot.backend.apirest.challenge.exception.ResourceNotFoundException;
import com.rivera.springboot.backend.apirest.challenge.service.TipoDocumentoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/tipo-documento")
@Tag(name = "Tipo Documento", description = "APIs para gestionar los tipos de documentos. Requieren autenticación mediante token JWT.")
@SecurityRequirement(name = "bearerAuth")
public class TipoDocumentoController {

	@Autowired
	private TipoDocumentoService tipoDocumentoService;

	@Operation(summary = "Obtener todos los tipos de documentos", description = "Devuelve una lista de todos los tipos de documentos registrados.", responses = {
			@ApiResponse(responseCode = "200", description = "Lista obtenida con éxito."),
			@ApiResponse(responseCode = "401", description = "No autorizado. Token JWT ausente o inválido.", content = @Content),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content) })
	@GetMapping
	public ResponseEntity<?> findAll() {
		List<TipoDocumento> tiposDocumento = tipoDocumentoService.findAll();

		Map<String, Object> response = new HashMap<>();
		response.put("message", "Registros encontrados!");
		response.put("data", tiposDocumento);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(summary = "Obtener tipos de documentos por estado", description = "Devuelve una lista de tipos de documentos filtrados por estado (activo/inactivo).", responses = {
			@ApiResponse(responseCode = "200", description = "Lista filtrada obtenida con éxito."),
			@ApiResponse(responseCode = "401", description = "No autorizado. Token JWT ausente o inválido.", content = @Content),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content) })
	@GetMapping("/estado/{estado}")
	public ResponseEntity<?> findAll(@PathVariable Boolean estado) {
		List<TipoDocumento> tiposDocumento = tipoDocumentoService.findAll(estado);

		Map<String, Object> response = new HashMap<>();
		response.put("message", "Registros encontrados!");
		response.put("data", tiposDocumento);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(summary = "Obtener un tipo de documento por ID", description = "Devuelve la información del tipo de documento especificado por su ID.", responses = {
			@ApiResponse(responseCode = "200", description = "Registro encontrado con éxito."),
			@ApiResponse(responseCode = "404", description = "No se encontró el tipo de documento con el ID especificado.", content = @Content),
			@ApiResponse(responseCode = "401", description = "No autorizado. Token JWT ausente o inválido.", content = @Content) })
	@GetMapping("/{tipoDocumentoId}")
	public ResponseEntity<?> findById(@PathVariable Long tipoDocumentoId) {
		TipoDocumento tipoDocumentoEncontrado = tipoDocumentoService.findById(tipoDocumentoId);

		if (tipoDocumentoEncontrado == null) {
			throw new ResourceNotFoundException("El tipo de documento con el ID (" + tipoDocumentoId + ") no existe.");
		}

		Map<String, Object> response = new HashMap<>();
		response.put("message", "Registro encontrado!");
		response.put("data", tipoDocumentoEncontrado);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(summary = "Crear un nuevo tipo de documento", description = "Registra un nuevo tipo de documento con los datos proporcionados.", responses = {
			@ApiResponse(responseCode = "201", description = "Tipo de documento creado con éxito."),
			@ApiResponse(responseCode = "400", description = "Datos inválidos o tipo de documento duplicado.", content = @Content),
			@ApiResponse(responseCode = "401", description = "No autorizado. Token JWT ausente o inválido.", content = @Content) })
	@PostMapping
	public ResponseEntity<?> save(@Valid @RequestBody TipoDocumentoDTO tipoDocumento, BindingResult result) {
		if (result.hasErrors()) {
			throw new ArgumentNotValidException(result);
		}

		TipoDocumento tipoDocumentoDuplicado = tipoDocumentoService.findByCodigo(tipoDocumento.getCodigo());

		if (tipoDocumentoDuplicado != null) {
			throw new DuplicateRecordOrBadRequestException(
					"El tipo de documento con el código (" + tipoDocumento.getCodigo() + ") ya existe.");
		}

		TipoDocumento nuevoTipoDocumento = new TipoDocumento();
		nuevoTipoDocumento.setCodigo(tipoDocumento.getCodigo());
		nuevoTipoDocumento.setNombre(tipoDocumento.getNombre());
		nuevoTipoDocumento.setDescripcion(tipoDocumento.getDescripcion());
		nuevoTipoDocumento.setEstado(tipoDocumento.getEstado() != null ? tipoDocumento.getEstado() : true);

		tipoDocumentoService.save(nuevoTipoDocumento);

		Map<String, Object> response = new HashMap<>();
		response.put("message", "El tipo de documento ha sido creado con éxito!");
		response.put("data", nuevoTipoDocumento);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@Operation(summary = "Actualizar un tipo de documento", description = "Actualiza la información de un tipo de documento existente.", responses = {
			@ApiResponse(responseCode = "200", description = "Tipo de documento actualizado con éxito."),
			@ApiResponse(responseCode = "404", description = "No se encontró el tipo de documento con el ID especificado.", content = @Content),
			@ApiResponse(responseCode = "401", description = "No autorizado. Token JWT ausente o inválido.", content = @Content) })
	@PutMapping("/{tipoDocumentoId}")
	public ResponseEntity<?> update(@PathVariable Long tipoDocumentoId,
			@Valid @RequestBody TipoDocumentoDTO tipoDocumento, BindingResult result) {
		if (result.hasErrors()) {
			throw new ArgumentNotValidException(result);
		}

		TipoDocumento tipoDocumentoEncontrado = tipoDocumentoService.findById(tipoDocumentoId);

		if (tipoDocumentoEncontrado == null) {
			throw new ResourceNotFoundException("El tipo de documento con el ID (" + tipoDocumentoId + ") no existe.");
		}

		TipoDocumento tipoDocumentoDuplicado = tipoDocumentoService.findByCodigo(tipoDocumento.getCodigo());

		if (tipoDocumentoDuplicado != null && !tipoDocumentoDuplicado.getId().equals(tipoDocumentoEncontrado.getId())) {
			throw new DuplicateRecordOrBadRequestException(
					"El tipo de documento con el código (" + tipoDocumento.getCodigo() + ") ya existe.");
		}

		tipoDocumentoEncontrado.setCodigo(tipoDocumento.getCodigo());
		tipoDocumentoEncontrado.setNombre(tipoDocumento.getNombre());
		tipoDocumentoEncontrado.setDescripcion(tipoDocumento.getDescripcion());
		tipoDocumentoEncontrado.setEstado(
				tipoDocumento.getEstado() != null ? tipoDocumento.getEstado() : tipoDocumentoEncontrado.getEstado());

		tipoDocumentoService.save(tipoDocumentoEncontrado);

		Map<String, Object> response = new HashMap<>();
		response.put("message", "El tipo de documento ha sido actualizado con éxito!");
		response.put("data", tipoDocumentoEncontrado);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(summary = "Eliminar un tipo de documento", description = "Elimina un tipo de documento por su ID.", responses = {
			@ApiResponse(responseCode = "200", description = "Tipo de documento eliminado con éxito."),
			@ApiResponse(responseCode = "404", description = "No se encontró el tipo de documento con el ID especificado.", content = @Content),
			@ApiResponse(responseCode = "401", description = "No autorizado. Token JWT ausente o inválido.", content = @Content) })
	@DeleteMapping("/{tipoDocumentoId}")
	public ResponseEntity<?> deleteById(@PathVariable Long tipoDocumentoId) {
		TipoDocumento tipoDocumentoEncontrado = tipoDocumentoService.findById(tipoDocumentoId);

		if (tipoDocumentoEncontrado == null) {
			throw new ResourceNotFoundException("El tipo de documento con el ID (" + tipoDocumentoId + ") no existe.");
		}

		tipoDocumentoService.deleteById(tipoDocumentoId);

		Map<String, Object> response = new HashMap<>();
		response.put("message", "El tipo de documento ha sido eliminado con éxito!");

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
