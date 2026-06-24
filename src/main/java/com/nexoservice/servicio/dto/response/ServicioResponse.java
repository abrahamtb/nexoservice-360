package com.nexoservice.servicio.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ServicioResponse(
        Long id,
        String nombre,
        String descripcion,
        BigDecimal precioBase,
        Integer duracionEstimadaMinutos,
        Boolean activo,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion
) {
}
