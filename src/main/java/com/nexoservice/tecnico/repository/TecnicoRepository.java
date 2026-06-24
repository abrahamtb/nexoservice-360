package com.nexoservice.tecnico.repository;

import com.nexoservice.tecnico.entity.DisponibilidadTecnico;
import com.nexoservice.tecnico.entity.Tecnico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {

    boolean existsByCorreo(String correo);

    Optional<Tecnico> findByCorreo(String correo);

    boolean existsByCorreoAndIdNot(String correo, Long id);

    Page<Tecnico> findByActivoTrueAndDisponibilidad(DisponibilidadTecnico disponibilidad, Pageable pageable);

    Page<Tecnico> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCaseOrEspecialidadContainingIgnoreCaseOrCorreoContainingIgnoreCase(
            String nombres,
            String apellidos,
            String especialidad,
            String correo,
            Pageable pageable
    );
}
