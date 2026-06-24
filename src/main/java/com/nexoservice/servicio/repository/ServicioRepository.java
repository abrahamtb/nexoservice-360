package com.nexoservice.servicio.repository;

import com.nexoservice.servicio.entity.Servicio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);

    Page<Servicio> findByActivoTrue(Pageable pageable);

    Page<Servicio> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(
            String nombre,
            String descripcion,
            Pageable pageable
    );
}
