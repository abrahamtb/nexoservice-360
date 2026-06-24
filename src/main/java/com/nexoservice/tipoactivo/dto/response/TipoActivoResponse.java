package com.nexoservice.tipoactivo.dto.response;

import java.time.LocalDateTime;

public record TipoActivoResponse(
        Long id,
        String nombre,
        String descripcion,
        Boolean activo,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion
) {
}
