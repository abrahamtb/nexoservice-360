package com.nexoservice.mantenimiento.mapper;

import com.nexoservice.equipo.entity.Equipo;
import com.nexoservice.mantenimiento.dto.request.MantenimientoProgramadoCreateRequest;
import com.nexoservice.mantenimiento.dto.response.MantenimientoProgramadoResponse;
import com.nexoservice.mantenimiento.entity.MantenimientoProgramado;
import com.nexoservice.servicio.entity.Servicio;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MantenimientoProgramadoMapper {

    public MantenimientoProgramado toEntity(MantenimientoProgramadoCreateRequest request, Equipo equipo, Servicio servicio) {
        return MantenimientoProgramado.builder()
                .equipo(equipo)
                .servicio(servicio)
                .frecuencia(request.frecuencia())
                .fechaInicio(request.fechaInicio())
                .proximaFecha(request.proximaFecha() == null ? request.fechaInicio() : request.proximaFecha())
                .observaciones(request.observaciones())
                .build();
    }

    public MantenimientoProgramadoResponse toResponse(MantenimientoProgramado mantenimiento) {
        LocalDate hoy = LocalDate.now();
        LocalDate limiteAlerta = hoy.plusDays(7);
        Equipo equipo = mantenimiento.getEquipo();

        return new MantenimientoProgramadoResponse(
                mantenimiento.getId(),
                equipo.getId(),
                equipo.getTipo() + " " + equipo.getMarca() + (equipo.getModelo() == null ? "" : " " + equipo.getModelo()),
                equipo.getCliente().getId(),
                equipo.getCliente().getNombres() + " " + (equipo.getCliente().getApellidos() == null ? "" : equipo.getCliente().getApellidos()),
                mantenimiento.getServicio().getId(),
                mantenimiento.getServicio().getNombre(),
                mantenimiento.getFrecuencia(),
                mantenimiento.getEstado(),
                mantenimiento.getFechaInicio(),
                mantenimiento.getProximaFecha(),
                mantenimiento.getProximaFecha().isBefore(hoy),
                !mantenimiento.getProximaFecha().isAfter(limiteAlerta),
                mantenimiento.getUltimaOrdenId(),
                mantenimiento.getObservaciones(),
                mantenimiento.getFechaCreacion(),
                mantenimiento.getFechaActualizacion()
        );
    }
}
