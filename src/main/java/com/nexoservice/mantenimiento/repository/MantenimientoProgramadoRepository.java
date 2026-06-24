package com.nexoservice.mantenimiento.repository;

import com.nexoservice.mantenimiento.entity.EstadoMantenimientoProgramado;
import com.nexoservice.mantenimiento.entity.MantenimientoProgramado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface MantenimientoProgramadoRepository extends JpaRepository<MantenimientoProgramado, Long> {

    Page<MantenimientoProgramado> findByEstado(EstadoMantenimientoProgramado estado, Pageable pageable);

    Page<MantenimientoProgramado> findByEstadoAndProximaFechaLessThanEqual(
            EstadoMantenimientoProgramado estado,
            LocalDate fecha,
            Pageable pageable
    );
}
