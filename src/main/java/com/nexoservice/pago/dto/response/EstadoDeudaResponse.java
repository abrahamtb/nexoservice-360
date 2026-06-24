package com.nexoservice.pago.dto.response;

import com.nexoservice.pago.entity.EstadoDeuda;

import java.math.BigDecimal;

public record EstadoDeudaResponse(
        Long ordenServicioId,
        Long cotizacionAprobadaId,
        String numeroCotizacion,
        BigDecimal total,
        BigDecimal pagado,
        BigDecimal saldo,
        EstadoDeuda estado
) {
}
