package com.nexoservice.inventario.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductoInventarioResponse(
        Long id,
        String codigo,
        String nombre,
        String descripcion,
        String categoria,
        String unidadMedida,
        Integer stockActual,
        Integer stockMinimo,
        Boolean bajoStock,
        BigDecimal costoUnitario,
        BigDecimal precioReferencia,
        Boolean activo,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion
) {
}
