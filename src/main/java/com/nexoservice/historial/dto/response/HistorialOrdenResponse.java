package com.nexoservice.historial.dto.response;

import com.nexoservice.orden.entity.EstadoOrden;

import java.time.LocalDateTime;

public record HistorialOrdenResponse(
        Long id,
        Long ordenServicioId,
        EstadoOrden estadoAnterior,
        EstadoOrden estadoNuevo,
        LocalDateTime fechaCambio,
        String usuarioResponsable,
        String observacion
) {
}
