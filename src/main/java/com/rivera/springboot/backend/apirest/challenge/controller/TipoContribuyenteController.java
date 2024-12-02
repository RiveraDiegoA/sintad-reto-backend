package com.rivera.springboot.backend.apirest.challenge.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.rivera.springboot.backend.apirest.challenge.dto.TipoContribuyenteDTO;
import com.rivera.springboot.backend.apirest.challenge.entity.TipoContribuyente;
import com.rivera.springboot.backend.apirest.challenge.exception.ArgumentNotValidException;
import com.rivera.springboot.backend.apirest.challenge.exception.DuplicateRecordOrBadRequestException;
import com.rivera.springboot.backend.apirest.challenge.exception.ResourceNotFoundException;
import com.rivera.springboot.backend.apirest.challenge.service.TipoContribuyenteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/tipo-contribuyente")
@Tag(name = "Tipo Contribuyente", description = "APIs para gestionar los tipos de contribuyentes. Requieren autenticación mediante token JWT.")
@SecurityRequirement(name = "bearerAuth")
public class TipoContribuyenteController {

	@Autowired
	private TipoContribuyenteService tipoContribuyenteService;

	@Operation(summary = "Obtener todos los tipos de contribuyentes", description = "Devuelve una lista de todos los tipos de contribuyentes registrados.", responses = {
			@ApiResponse(responseCode = "200", description = "Lista obtenida con éxito."),
			@ApiResponse(responseCode = "401", description = "No autorizado. Token JWT ausente o inválido.", content = @Content),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content) })
	@GetMapping
	public ResponseEntity<?> findAll() {
		List<TipoContribuyente> tiposContribuyente = tipoContribuyenteService.findAll();

		Map<String, Object> response = new HashMap<>();
		response.put("message", "Registros encontrados!");
		response.put("data", tiposContribuyente);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(summary = "Obtener tipos de contribuyentes por estado", description = "Devuelve una lista de tipos de contribuyentes filtrados por estado (activo/inactivo).", responses = {
			@ApiResponse(responseCode = "200", description = "Lista filtrada obtenida con éxito."),
			@ApiResponse(responseCode = "401", description = "No autorizado. Token JWT ausente o inválido.", content = @Content),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content) })
	@GetMapping("/estado/{estado}")
	public ResponseEntity<?> findAll(@PathVariable Boolean estado) {
		List<TipoContribuyente> tiposContribuyente = tipoContribuyenteService.findAll(estado);

		Map<String, Object> response = new HashMap<>();
		response.put("message", "Registros encontrados!");
		response.put("data", tiposContribuyente);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(summary = "Obtener un tipo de contribuyente por ID", description = "Devuelve la información del tipo de contribuyente especificado por su ID.", responses = {
			@ApiResponse(responseCode = "200", description = "Registro encontrado con éxito."),
			@ApiResponse(responseCode = "404", description = "No se encontró el tipo de contribuyente con el ID especificado.", content = @Content),
			@ApiResponse(responseCode = "401", description = "No autorizado. Token JWT ausente o inválido.", content = @Content) })
	@GetMapping("/{tipoContribuyenteId}")
	public ResponseEntity<?> findById(@PathVariable Long tipoContribuyenteId) {
		TipoContribuyente tipoContribuyenteEncontrado = tipoContribuyenteService.findById(tipoContribuyenteId);

		if (tipoContribuyenteEncontrado == null) {
			throw new ResourceNotFoundException(
					"El tipo de contribuyente con el ID (" + tipoContribuyenteId + ") no existe.");
		}

		Map<String, Object> response = new HashMap<>();
		response.put("message", "Registro encontrado!");
		response.put("data", tipoContribuyenteEncontrado);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(summary = "Crear un nuevo tipo de contribuyente", description = "Registra un nuevo tipo de contribuyente con los datos proporcionados.", responses = {
			@ApiResponse(responseCode = "201", description = "Tipo de contribuyente creado con éxito."),
			@ApiResponse(responseCode = "400", description = "Datos inválidos o tipo de contribuyente duplicado.", content = @Content),
			@ApiResponse(responseCode = "401", description = "No autorizado. Token JWT ausente o inválido.", content = @Content) })
	@PostMapping
	public ResponseEntity<?> save(@Valid @RequestBody TipoContribuyente tipoContribuyente, BindingResult result) {
		if (result.hasErrors()) {
			throw new ArgumentNotValidException(result);
		}

		TipoContribuyente tipoContribuyenteDuplicado = tipoContribuyenteService
				.findByNombre(tipoContribuyente.getNombre());

		if (tipoContribuyenteDuplicado != null) {
			throw new DuplicateRecordOrBadRequestException(
					"El tipo de contribuyente con el nombre (" + tipoContribuyente.getNombre() + ") ya existe.");
		}

		TipoContribuyente nuevoTipoContribuyente = new TipoContribuyente();
		nuevoTipoContribuyente.setNombre(tipoContribuyente.getNombre());
		nuevoTipoContribuyente.setEstado(tipoContribuyente.getEstado() != null ? tipoContribuyente.getEstado()
				: nuevoTipoContribuyente.getEstado());

		tipoContribuyenteService.save(nuevoTipoContribuyente);

		Map<String, Object> response = new HashMap<>();
		response.put("message", "El tipo de contribuyente ha sido creado con éxito!");
		response.put("data", nuevoTipoContribuyente);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@Operation(summary = "Actualizar un tipo de contribuyente", description = "Actualiza la información de un tipo de contribuyente existente.", responses = {
			@ApiResponse(responseCode = "200", description = "Tipo de contribuyente actualizado con éxito."),
			@ApiResponse(responseCode = "404", description = "No se encontró el tipo de contribuyente con el ID especificado.", content = @Content),
			@ApiResponse(responseCode = "401", description = "No autorizado. Token JWT ausente o inválido.", content = @Content) })
	@PutMapping("/{tipoContribuyenteId}")
	public ResponseEntity<?> update(@PathVariable Long tipoContribuyenteId,
			@Valid @RequestBody TipoContribuyenteDTO tipoContribuyente, BindingResult result) {
		if (result.hasErrors()) {
			throw new ArgumentNotValidException(result);
		}

		TipoContribuyente tipoContribuyenteEncontrado = tipoContribuyenteService.findById(tipoContribuyenteId);

		if (tipoContribuyenteEncontrado == null) {
			throw new ResourceNotFoundException(
					"El tipo de contribuyente con el ID (" + tipoContribuyenteId + ") no existe.");
		}

		TipoContribuyente tipoContribuyenteDuplicado = tipoContribuyenteService
				.findByNombre(tipoContribuyente.getNombre());

		if (tipoContribuyenteDuplicado != null
				&& !tipoContribuyenteDuplicado.getId().equals(tipoContribuyenteEncontrado.getId())) {
			throw new DuplicateRecordOrBadRequestException(
					"El tipo de contribuyente con el nombre (" + tipoContribuyente.getNombre() + ") ya existe.");
		}

		tipoContribuyenteEncontrado.setNombre(tipoContribuyente.getNombre());
		tipoContribuyenteEncontrado.setEstado(tipoContribuyente.getEstado() != null ? tipoContribuyente.getEstado()
				: tipoContribuyenteEncontrado.getEstado());

		tipoContribuyenteService.save(tipoContribuyenteEncontrado);

		Map<String, Object> response = new HashMap<>();
		response.put("message", "El tipo de contribuyente ha sido actualizado con éxito!");
		response.put("data", tipoContribuyenteEncontrado);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(summary = "Eliminar un tipo de contribuyente", description = "Elimina un tipo de contribuyente por su ID.", responses = {
			@ApiResponse(responseCode = "200", description = "Tipo de contribuyente eliminado con éxito."),
			@ApiResponse(responseCode = "404", description = "No se encontró el tipo de contribuyente con el ID especificado.", content = @Content),
			@ApiResponse(responseCode = "401", description = "No autorizado. Token JWT ausente o inválido.", content = @Content) })
	@DeleteMapping("/{tipoContribuyenteId}")
	public ResponseEntity<?> deleteById(@PathVariable Long tipoContribuyenteId) {
		TipoContribuyente tipoContribuyenteEncontrado = tipoContribuyenteService.findById(tipoContribuyenteId);

		if (tipoContribuyenteEncontrado == null) {
			throw new ResourceNotFoundException(
					"El tipo de contribuyente con el ID (" + tipoContribuyenteId + ") no existe.");
		}

		tipoContribuyenteService.deleteById(tipoContribuyenteId);

		Map<String, Object> response = new HashMap<>();
		response.put("message", "El tipo de contribuyente ha sido eliminado con éxito!");

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
