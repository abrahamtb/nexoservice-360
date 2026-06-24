package com.nexoservice.tipoactivo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TipoActivoUpdateRequest(
        @NotBlank(message = "El nombre del tipo de activo es obligatorio")
        @Size(max = 120, message = "El nombre no debe superar 120 caracteres")
        String nombre,

        @Size(max = 300, message = "La descripcion no debe superar 300 caracteres")
        String descripcion
) {
}
