package com.nexoservice.evidencia.repository;

import com.nexoservice.evidencia.entity.EvidenciaOrden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvidenciaOrdenRepository extends JpaRepository<EvidenciaOrden, Long> {

    List<EvidenciaOrden> findByOrdenServicioIdOrderByFechaRegistroDesc(Long ordenServicioId);
}
