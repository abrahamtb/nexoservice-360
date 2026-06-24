package com.nexoservice.orden.dto.request;

import com.nexoservice.orden.entity.PrioridadOrden;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record OrdenServicioUpdateRequest(
        @NotNull(message = "La prioridad es obligatoria")
        PrioridadOrden prioridad,

        @FutureOrPresent(message = "La fecha estimada no puede estar en el pasado")
        LocalDate fechaEstimada,

        @NotBlank(message = "La descripcion del problema es obligatoria")
        @Size(max = 500, message = "La descripcion del problema no debe superar 500 caracteres")
        String descripcionProblema,

        @Size(max = 500, message = "Las observaciones no deben superar 500 caracteres")
        String observaciones
) {
}
