package com.nexoservice.diagnostico.dto.response;

import java.time.LocalDateTime;

public record DiagnosticoResponse(
        Long id,
        Long ordenServicioId,
        String descripcionFalla,
        String causaProbable,
        String solucionRecomendada,
        String observaciones,
        LocalDateTime fechaDiagnostico,
        String usuarioResponsable,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion
) {
}
