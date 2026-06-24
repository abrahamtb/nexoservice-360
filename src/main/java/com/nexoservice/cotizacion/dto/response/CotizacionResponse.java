package com.nexoservice.cotizacion.dto.response;

import com.nexoservice.cotizacion.entity.EstadoCotizacion;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record CotizacionResponse(
        Long id,
        String numero,
        Long ordenServicioId,
        String clienteNombre,
        String activoResumen,
        LocalDate fechaEmision,
        LocalDate fechaVencimiento,
        EstadoCotizacion estado,
        BigDecimal subtotal,
        BigDecimal impuesto,
        BigDecimal total,
        String observaciones,
        List<CotizacionDetalleResponse> detalles,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion
) {
}
