package com.nexoservice.equipo.dto.response;

import com.nexoservice.equipo.entity.EstadoEquipo;

import java.time.LocalDateTime;

public record EquipoResponse(
        Long id,
        Long clienteId,
        String clienteNombre,
        Long tipoActivoId,
        String tipoActivoNombre,
        String tipo,
        String marca,
        String modelo,
        String numeroSerie,
        String codigoInterno,
        String ubicacion,
        String caracteristicas,
        String descripcion,
        String observaciones,
        EstadoEquipo estado,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion
) {
}
