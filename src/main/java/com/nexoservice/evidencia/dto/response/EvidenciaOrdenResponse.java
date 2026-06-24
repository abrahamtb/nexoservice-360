package com.nexoservice.evidencia.dto.response;

import com.nexoservice.evidencia.entity.TipoEvidencia;

import java.time.LocalDateTime;

public record EvidenciaOrdenResponse(
        Long id,
        Long ordenServicioId,
        Long tecnicoId,
        String tecnicoNombre,
        TipoEvidencia tipo,
        String nombreArchivo,
        String urlArchivo,
        String observaciones,
        String usuarioResponsable,
        LocalDateTime fechaRegistro
) {
}
