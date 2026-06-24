package com.nexoservice.pago.dto.response;

import com.nexoservice.pago.entity.EstadoPago;
import com.nexoservice.pago.entity.MetodoPago;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagoResponse(
        Long id,
        Long ordenServicioId,
        Long cotizacionId,
        String numeroCotizacion,
        BigDecimal monto,
        MetodoPago metodoPago,
        EstadoPago estado,
        String numeroOperacion,
        String observaciones,
        String usuarioResponsable,
        LocalDateTime fechaPago,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion
) {
}
