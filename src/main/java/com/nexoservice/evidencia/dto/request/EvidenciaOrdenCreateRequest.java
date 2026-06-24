package com.nexoservice.evidencia.dto.request;

import com.nexoservice.evidencia.entity.TipoEvidencia;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EvidenciaOrdenCreateRequest(
        @NotNull(message = "La orden de servicio es obligatoria")
        Long ordenServicioId,

        Long tecnicoId,

        @NotNull(message = "El tipo de evidencia es obligatorio")
        TipoEvidencia tipo,

        @Size(max = 180, message = "El nombre del archivo no debe superar 180 caracteres")
        String nombreArchivo,

        @Size(max = 500, message = "La URL o ruta del archivo no debe superar 500 caracteres")
        String urlArchivo,

        @Size(max = 700, message = "Las observaciones no deben superar 700 caracteres")
        String observaciones,

        @Size(max = 120, message = "El usuario responsable no debe superar 120 caracteres")
        String usuarioResponsable
) {
}
