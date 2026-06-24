package com.nexoservice.tecnico.dto.response;

import com.nexoservice.tecnico.entity.DisponibilidadTecnico;

import java.time.LocalDateTime;

public record TecnicoResponse(
        Long id,
        String nombres,
        String apellidos,
        String especialidad,
        String correo,
        String telefono,
        DisponibilidadTecnico disponibilidad,
        Boolean activo,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion
) {
}
