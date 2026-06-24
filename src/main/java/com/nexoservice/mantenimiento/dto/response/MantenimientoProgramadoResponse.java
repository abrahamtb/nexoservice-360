package com.nexoservice.mantenimiento.dto.response;

import com.nexoservice.mantenimiento.entity.EstadoMantenimientoProgramado;
import com.nexoservice.mantenimiento.entity.FrecuenciaMantenimiento;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record MantenimientoProgramadoResponse(
        Long id,
        Long equipoId,
        String equipoResumen,
        Long clienteId,
        String clienteNombre,
        Long servicioId,
        String servicioNombre,
        FrecuenciaMantenimiento frecuencia,
        EstadoMantenimientoProgramado estado,
        LocalDate fechaInicio,
        LocalDate proximaFecha,
        boolean vencido,
        boolean alerta,
        Long ultimaOrdenId,
        String observaciones,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion
) {
}
