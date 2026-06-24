package com.nexoservice.inventario.dto.request;

import jakarta.validation.constraints.NotNull;

public record ProductoInventarioEstadoRequest(
        @NotNull(message = "El estado activo es obligatorio")
        Boolean activo
) {
}
