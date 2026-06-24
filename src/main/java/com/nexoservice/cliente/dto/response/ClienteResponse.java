package com.nexoservice.cliente.dto.response;

import com.nexoservice.cliente.entity.TipoDocumento;

import java.time.LocalDateTime;

public record ClienteResponse(
        Long id,
        String nombres,
        String apellidos,
        TipoDocumento tipoDocumento,
        String numeroDocumento,
        String correo,
        String telefono,
        String direccion,
        Boolean activo,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion
) {
}
