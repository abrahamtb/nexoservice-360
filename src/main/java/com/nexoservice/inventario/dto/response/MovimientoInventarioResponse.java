package com.nexoservice.inventario.dto.response;

import com.nexoservice.inventario.entity.TipoMovimientoInventario;

import java.time.LocalDateTime;

public record MovimientoInventarioResponse(
        Long id,
        Long productoId,
        String productoCodigo,
        String productoNombre,
        Long ordenServicioId,
        TipoMovimientoInventario tipoMovimiento,
        Integer cantidad,
        Integer stockResultante,
        String motivo,
        String usuarioResponsable,
        LocalDateTime fechaMovimiento
) {
}
