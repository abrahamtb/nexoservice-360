package com.nexoservice.inventario.repository;

import com.nexoservice.inventario.entity.MovimientoInventario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {

    Page<MovimientoInventario> findByProductoIdOrderByFechaMovimientoDesc(Long productoId, Pageable pageable);

    Page<MovimientoInventario> findByOrdenServicioIdOrderByFechaMovimientoDesc(Long ordenServicioId, Pageable pageable);
}
