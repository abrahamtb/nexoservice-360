package com.nexoservice.equipo.dto.request;

import com.nexoservice.equipo.entity.EstadoEquipo;
import jakarta.validation.constraints.NotNull;

public record EquipoEstadoRequest(
        @NotNull(message = "El estado del activo es obligatorio")
        EstadoEquipo estado
) {
}
