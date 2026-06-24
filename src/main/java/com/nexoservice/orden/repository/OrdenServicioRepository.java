package com.nexoservice.orden.repository;

import com.nexoservice.orden.entity.EstadoOrden;
import com.nexoservice.orden.entity.OrdenServicio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdenServicioRepository extends JpaRepository<OrdenServicio, Long> {

    long countByEstado(EstadoOrden estado);

    long countByTecnicoId(Long tecnicoId);

    long countByTecnicoIdAndEstado(Long tecnicoId, EstadoOrden estado);

    Page<OrdenServicio> findByEstado(EstadoOrden estado, Pageable pageable);

    Page<OrdenServicio> findByClienteId(Long clienteId, Pageable pageable);

    Page<OrdenServicio> findByTecnicoId(Long tecnicoId, Pageable pageable);

    Page<OrdenServicio> findByTecnicoIdAndEstado(Long tecnicoId, EstadoOrden estado, Pageable pageable);
}
