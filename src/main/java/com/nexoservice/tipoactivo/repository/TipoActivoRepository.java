package com.nexoservice.tipoactivo.repository;

import com.nexoservice.tipoactivo.entity.TipoActivo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoActivoRepository extends JpaRepository<TipoActivo, Long> {

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);

    Page<TipoActivo> findByActivoTrue(Pageable pageable);

    Page<TipoActivo> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(
            String nombre,
            String descripcion,
            Pageable pageable
    );
}
