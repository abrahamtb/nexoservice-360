package com.nexoservice.tipoactivo.dto.request;

import jakarta.validation.constraints.NotNull;

public record TipoActivoEstadoRequest(
        @NotNull(message = "El estado activo es obligatorio")
        Boolean activo
) {
}
