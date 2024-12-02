package com.rivera.springboot.backend.apirest.challenge.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.rivera.springboot.backend.apirest.challenge.entity.Usuario;
import com.rivera.springboot.backend.apirest.challenge.service.UsuarioService;

@Component
@Configuration
public class AdminInitializer {

	@Bean
	CommandLineRunner init(UsuarioService usuarioService) {
		return args -> {
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

			if (usuarioService.findByNombreUsuario("admin1") == null) {
				Usuario admin1 = new Usuario();
				admin1.setNombreUsuario("admin1");
				admin1.setContrasena(passwordEncoder.encode("admin123"));
				admin1.setRol("ROL_ADMIN");
				admin1.setEstado(true);
				usuarioService.save(admin1);
				System.out.println("Administrador admin1 creado.");
			}

			if (usuarioService.findByNombreUsuario("admin2") == null) {
				Usuario admin2 = new Usuario();
				admin2.setNombreUsuario("admin2");
				admin2.setContrasena(passwordEncoder.encode("admin123"));
				admin2.setRol("ROL_ADMIN");
				admin2.setEstado(true);
				usuarioService.save(admin2);
				System.out.println("Administrador admin creado.");
			}
		};
	}
}
