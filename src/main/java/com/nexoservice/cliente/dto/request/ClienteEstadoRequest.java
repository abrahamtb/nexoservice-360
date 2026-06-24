package com.nexoservice.cliente.dto.request;

import jakarta.validation.constraints.NotNull;

public record ClienteEstadoRequest(
        @NotNull(message = "El estado activo es obligatorio")
        Boolean activo
) {
}
