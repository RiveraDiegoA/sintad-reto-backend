package com.rivera.springboot.backend.apirest.challenge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.rivera.springboot.backend.apirest.challenge.dto.AuthDTO;
import com.rivera.springboot.backend.apirest.challenge.entity.Usuario;
import com.rivera.springboot.backend.apirest.challenge.exception.ArgumentNotValidException;
import com.rivera.springboot.backend.apirest.challenge.exception.DuplicateRecordOrBadRequestException;
import com.rivera.springboot.backend.apirest.challenge.service.UsuarioService;
import com.rivera.springboot.backend.apirest.challenge.util.JwtUtil;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticación", description = "APIs relacionadas con la autenticación de usuarios.")
public class AuthController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private JwtUtil jwtUtil;

	@Operation(summary = "Iniciar sesión", description = "Permite a un usuario autenticarse y obtener un token JWT para acceder a las demás APIs protegidas.", responses = {
			@ApiResponse(responseCode = "200", description = "Autenticación exitosa. Devuelve un token JWT y los datos del usuario autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
			@ApiResponse(responseCode = "400", description = "Datos inválidos o credenciales incorrectas.", content = @Content) })
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody AuthDTO auth, BindingResult result) {
		if (result.hasErrors()) {
			throw new ArgumentNotValidException(result);
		}

		Usuario usuarioEncontrado = usuarioService.findByNombreUsuario(auth.getUsername());

		if (usuarioEncontrado == null
				|| !new BCryptPasswordEncoder().matches(auth.getPassword(), usuarioEncontrado.getContrasena())) {
			throw new DuplicateRecordOrBadRequestException("Credenciales incorrectas, vuelva a intentarlo");
		}

		String token = jwtUtil.generateToken(auth.getUsername());

		Map<String, Object> response = new HashMap<>();
		response.put("token", token);
		response.put("data", usuarioEncontrado);

		return ResponseEntity.ok(response);
	}
}
