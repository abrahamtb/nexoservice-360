package com.nexoservice.cotizacion.dto.response;

import java.math.BigDecimal;

public record CotizacionDetalleResponse(
        Long id,
        String descripcion,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) {
}
