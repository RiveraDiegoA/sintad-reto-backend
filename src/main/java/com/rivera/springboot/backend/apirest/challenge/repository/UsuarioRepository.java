package com.rivera.springboot.backend.apirest.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rivera.springboot.backend.apirest.challenge.entity.Usuario;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	Optional<Usuario> findByNombreUsuario(String nombreUsuario);
}
