package com.nexoservice.orden.dto.request;

import com.nexoservice.orden.entity.EstadoOrden;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CambiarEstadoOrdenRequest(
        @NotNull(message = "El estado nuevo es obligatorio")
        EstadoOrden estadoNuevo,

        @Size(max = 120, message = "El usuario responsable no debe superar 120 caracteres")
        String usuarioResponsable,

        @Size(max = 500, message = "La observacion no debe superar 500 caracteres")
        String observacion
) {
}
