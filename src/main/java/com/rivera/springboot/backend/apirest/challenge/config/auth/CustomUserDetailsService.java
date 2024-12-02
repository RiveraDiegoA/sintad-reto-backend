package com.rivera.springboot.backend.apirest.challenge.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rivera.springboot.backend.apirest.challenge.entity.Usuario;
import com.rivera.springboot.backend.apirest.challenge.service.UsuarioService;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UsuarioService usuarioService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioService.findByNombreUsuario(username);

		if (usuario == null) {
			throw new UsernameNotFoundException("Usuario no encontrado: " + username);
		}

		return new User(usuario.getNombreUsuario(), usuario.getContrasena(), usuario.getEstado(), true, true, true,
				Collections.emptyList());
	}
}
