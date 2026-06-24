package com.nexoservice.tecnico.dto.request;

import jakarta.validation.constraints.NotNull;

public record TecnicoEstadoRequest(
        @NotNull(message = "El estado activo es obligatorio")
        Boolean activo
) {
}
