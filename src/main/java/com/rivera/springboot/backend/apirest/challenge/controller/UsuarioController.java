package com.rivera.springboot.backend.apirest.challenge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rivera.springboot.backend.apirest.challenge.dto.UsuarioDTO;
import com.rivera.springboot.backend.apirest.challenge.entity.Usuario;
import com.rivera.springboot.backend.apirest.challenge.exception.ArgumentNotValidException;
import com.rivera.springboot.backend.apirest.challenge.exception.DuplicateRecordOrBadRequestException;
import com.rivera.springboot.backend.apirest.challenge.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/usuario")
@Tag(name = "Usuario", description = "APIs para gestionar usuarios en el sistema. Requieren autenticación mediante token JWT.")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista de todos los usuarios registrados.", responses = {
			@ApiResponse(responseCode = "200", description = "Lista obtenida con éxito."),
			@ApiResponse(responseCode = "401", description = "No autorizado. Token JWT ausente o inválido.", content = @Content),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content) })
	@GetMapping
	public ResponseEntity<?> findAll() {
		List<Usuario> usuarios = usuarioService.findAll();
		Map<String, Object> response = new HashMap<>();

		response.put("message", "Registros encontrados!");
		response.put("data", usuarios);

		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Crear un nuevo usuario", description = "Registra un nuevo usuario con los datos proporcionados.", responses = {
			@ApiResponse(responseCode = "201", description = "Usuario creado con éxito."),
			@ApiResponse(responseCode = "400", description = "Datos inválidos o usuario duplicado.", content = @Content),
			@ApiResponse(responseCode = "401", description = "No autorizado. Token JWT ausente o inválido.", content = @Content) })
	@PostMapping
	public ResponseEntity<?> save(@Valid @RequestBody UsuarioDTO usuario, BindingResult result) {
		if (result.hasErrors()) {
			throw new ArgumentNotValidException(result);
		}

		Usuario usuarioDuplicado = usuarioService.findByNombreUsuario(usuario.getNombreUsuario());

		if (usuarioDuplicado != null) {
			throw new DuplicateRecordOrBadRequestException(
					"El usuario con el nombre de usuario (" + usuario.getNombreUsuario() + ") ya existe.");
		}

		Usuario nuevoUsuario = new Usuario();
		nuevoUsuario.setNombreUsuario(usuario.getNombreUsuario());
		nuevoUsuario.setContrasena(usuario.getContrasena());
		nuevoUsuario.setRol(usuario.getRol());
		nuevoUsuario.setEstado(usuario.getEstado() != null ? usuario.getEstado() : true);

		usuarioService.save(nuevoUsuario);

		Map<String, Object> response = new HashMap<>();
		response.put("message", "El usuario ha sido creado con éxito!");
		response.put("data", nuevoUsuario);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
}
