package com.nexoservice.pago.dto.request;

import com.nexoservice.pago.entity.MetodoPago;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record PagoCreateRequest(
        @NotNull(message = "La orden de servicio es obligatoria")
        Long ordenServicioId,

        Long cotizacionId,

        @NotNull(message = "El monto es obligatorio")
        @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero")
        BigDecimal monto,

        @NotNull(message = "El metodo de pago es obligatorio")
        MetodoPago metodoPago,

        @Size(max = 80, message = "El numero de operacion no debe superar 80 caracteres")
        String numeroOperacion,

        @Size(max = 500, message = "Las observaciones no deben superar 500 caracteres")
        String observaciones,

        @Size(max = 120, message = "El usuario responsable no debe superar 120 caracteres")
        String usuarioResponsable
) {
}
