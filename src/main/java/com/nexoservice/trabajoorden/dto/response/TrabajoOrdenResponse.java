package com.nexoservice.trabajoorden.dto.response;

import java.time.LocalDateTime;

public record TrabajoOrdenResponse(
        Long id,
        Long ordenServicioId,
        Long tecnicoId,
        String tecnicoNombre,
        String actividad,
        String detalle,
        LocalDateTime fechaTrabajo,
        Integer tiempoInvertidoMinutos,
        String observaciones,
        String usuarioResponsable,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion
) {
}
