package com.nexoservice.cotizacion.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CotizacionDetalleRequest(
        @NotBlank(message = "La descripcion del item es obligatoria")
        @Size(max = 250, message = "La descripcion no debe superar 250 caracteres")
        String descripcion,

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser mayor a cero")
        Integer cantidad,

        @NotNull(message = "El precio unitario es obligatorio")
        @DecimalMin(value = "0.00", inclusive = false, message = "El precio unitario debe ser mayor a cero")
        BigDecimal precioUnitario
) {
}
