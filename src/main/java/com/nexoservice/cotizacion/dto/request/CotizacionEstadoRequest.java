package com.nexoservice.cotizacion.dto.request;

import com.nexoservice.cotizacion.entity.EstadoCotizacion;
import jakarta.validation.constraints.NotNull;

public record CotizacionEstadoRequest(
        @NotNull(message = "El estado de la cotizacion es obligatorio")
        EstadoCotizacion estado
) {
}
