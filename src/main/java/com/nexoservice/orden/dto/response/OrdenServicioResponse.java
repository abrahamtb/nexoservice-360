package com.nexoservice.orden.dto.response;

import com.nexoservice.orden.entity.EstadoOrden;
import com.nexoservice.orden.entity.PrioridadOrden;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record OrdenServicioResponse(
        Long id,
        Long clienteId,
        String clienteNombre,
        Long equipoId,
        String equipoResumen,
        Long servicioId,
        String servicioNombre,
        Long tecnicoId,
        String tecnicoNombre,
        EstadoOrden estado,
        PrioridadOrden prioridad,
        LocalDateTime fechaIngreso,
        LocalDate fechaEstimada,
        String descripcionProblema,
        String observaciones,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion
) {
}
