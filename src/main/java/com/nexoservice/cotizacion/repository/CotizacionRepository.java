package com.nexoservice.cotizacion.repository;

import com.nexoservice.cotizacion.entity.Cotizacion;
import com.nexoservice.cotizacion.entity.EstadoCotizacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CotizacionRepository extends JpaRepository<Cotizacion, Long> {

    Page<Cotizacion> findByOrdenServicioId(Long ordenServicioId, Pageable pageable);

    Page<Cotizacion> findByEstado(EstadoCotizacion estado, Pageable pageable);

    Optional<Cotizacion> findFirstByOrdenServicioIdAndEstadoOrderByIdDesc(Long ordenServicioId, EstadoCotizacion estado);
}
