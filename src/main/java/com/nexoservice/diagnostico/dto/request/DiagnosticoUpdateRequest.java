package com.nexoservice.diagnostico.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DiagnosticoUpdateRequest(
        @NotBlank(message = "La descripcion de la falla es obligatoria")
        @Size(max = 700, message = "La descripcion de la falla no debe superar 700 caracteres")
        String descripcionFalla,

        @Size(max = 500, message = "La causa probable no debe superar 500 caracteres")
        String causaProbable,

        @NotBlank(message = "La solucion recomendada es obligatoria")
        @Size(max = 700, message = "La solucion recomendada no debe superar 700 caracteres")
        String solucionRecomendada,

        @Size(max = 500, message = "Las observaciones no deben superar 500 caracteres")
        String observaciones,

        @Size(max = 120, message = "El usuario responsable no debe superar 120 caracteres")
        String usuarioResponsable
) {
}
