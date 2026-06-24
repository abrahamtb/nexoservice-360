package com.nexoservice.tecnico.dto.request;

import com.nexoservice.tecnico.entity.DisponibilidadTecnico;
import jakarta.validation.constraints.NotNull;

public record TecnicoDisponibilidadRequest(
        @NotNull(message = "La disponibilidad es obligatoria")
        DisponibilidadTecnico disponibilidad
) {
}
