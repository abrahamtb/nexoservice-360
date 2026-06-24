package com.nexoservice.pago.repository;

import com.nexoservice.pago.entity.EstadoPago;
import com.nexoservice.pago.entity.Pago;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    Page<Pago> findByOrdenServicioId(Long ordenServicioId, Pageable pageable);

    Page<Pago> findByEstado(EstadoPago estado, Pageable pageable);

    long countByEstado(EstadoPago estado);

    @Query("""
            select coalesce(sum(p.monto), 0)
            from Pago p
            where p.ordenServicio.id = :ordenServicioId
              and p.estado = com.nexoservice.pago.entity.EstadoPago.REGISTRADO
            """)
    BigDecimal sumarPagosRegistradosPorOrden(@Param("ordenServicioId") Long ordenServicioId);
}
