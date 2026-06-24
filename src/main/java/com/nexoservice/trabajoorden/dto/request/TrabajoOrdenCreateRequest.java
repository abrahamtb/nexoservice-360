package com.nexoservice.trabajoorden.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TrabajoOrdenCreateRequest(
        @NotNull(message = "La orden de servicio es obligatoria")
        Long ordenServicioId,

        Long tecnicoId,

        @NotBlank(message = "La actividad es obligatoria")
        @Size(max = 160, message = "La actividad no debe superar 160 caracteres")
        String actividad,

        @NotBlank(message = "El detalle del trabajo es obligatorio")
        @Size(max = 1000, message = "El detalle no debe superar 1000 caracteres")
        String detalle,

        @Min(value = 1, message = "El tiempo invertido debe ser mayor a cero")
        Integer tiempoInvertidoMinutos,

        @Size(max = 500, message = "Las observaciones no deben superar 500 caracteres")
        String observaciones,

        @Size(max = 120, message = "El usuario responsable no debe superar 120 caracteres")
        String usuarioResponsable
) {
}
