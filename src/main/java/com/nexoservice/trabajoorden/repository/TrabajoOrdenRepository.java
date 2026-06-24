package com.nexoservice.trabajoorden.repository;

import com.nexoservice.trabajoorden.entity.TrabajoOrden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrabajoOrdenRepository extends JpaRepository<TrabajoOrden, Long> {

    List<TrabajoOrden> findByOrdenServicioIdOrderByFechaTrabajoAsc(Long ordenServicioId);
}
