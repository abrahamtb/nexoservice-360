package com.nexoservice.mantenimiento.dto.request;

import com.nexoservice.mantenimiento.entity.FrecuenciaMantenimiento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record MantenimientoProgramadoCreateRequest(
        @NotNull(message = "El activo es obligatorio")
        Long equipoId,

        @NotNull(message = "El servicio es obligatorio")
        Long servicioId,

        @NotNull(message = "La frecuencia es obligatoria")
        FrecuenciaMantenimiento frecuencia,

        @NotNull(message = "La fecha de inicio es obligatoria")
        LocalDate fechaInicio,

        LocalDate proximaFecha,

        @Size(max = 500, message = "Las observaciones no deben superar 500 caracteres")
        String observaciones
) {
}
