package com.nexoservice.cotizacion.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record CotizacionCreateRequest(
        @NotNull(message = "La orden de servicio es obligatoria")
        Long ordenServicioId,

        LocalDate fechaVencimiento,

        @Size(max = 500, message = "Las observaciones no deben superar 500 caracteres")
        String observaciones,

        @NotEmpty(message = "La cotizacion debe tener al menos un detalle")
        List<@Valid CotizacionDetalleRequest> detalles
) {
}
