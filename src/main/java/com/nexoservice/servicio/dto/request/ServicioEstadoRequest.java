package com.nexoservice.servicio.dto.request;

import jakarta.validation.constraints.NotNull;

public record ServicioEstadoRequest(
        @NotNull(message = "El estado activo es obligatorio")
        Boolean activo
) {
}
