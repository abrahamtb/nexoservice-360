package com.nexoservice.historial.repository;

import com.nexoservice.historial.entity.HistorialOrden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialOrdenRepository extends JpaRepository<HistorialOrden, Long> {

    List<HistorialOrden> findByOrdenServicioIdOrderByFechaCambioAsc(Long ordenServicioId);
}
