package com.nexoservice.usuario.repository;

import com.nexoservice.usuario.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByCorreo(String correo);

    boolean existsByUsernameAndIdNot(String username, Long id);

    boolean existsByCorreoAndIdNot(String correo, Long id);
}
