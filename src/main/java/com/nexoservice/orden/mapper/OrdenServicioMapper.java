package com.nexoservice.orden.mapper;

import com.nexoservice.cliente.entity.Cliente;
import com.nexoservice.equipo.entity.Equipo;
import com.nexoservice.orden.dto.request.OrdenServicioCreateRequest;
import com.nexoservice.orden.dto.request.OrdenServicioUpdateRequest;
import com.nexoservice.orden.dto.response.OrdenServicioResponse;
import com.nexoservice.orden.entity.OrdenServicio;
import com.nexoservice.orden.entity.PrioridadOrden;
import com.nexoservice.servicio.entity.Servicio;
import com.nexoservice.tecnico.entity.Tecnico;
import org.springframework.stereotype.Component;

@Component
public class OrdenServicioMapper {

    public OrdenServicio toEntity(
            OrdenServicioCreateRequest request,
            Cliente cliente,
            Equipo equipo,
            Servicio servicio,
            Tecnico tecnico
    ) {
        return OrdenServicio.builder()
                .cliente(cliente)
                .equipo(equipo)
                .servicio(servicio)
                .tecnico(tecnico)
                .prioridad(request.prioridad() == null ? PrioridadOrden.MEDIA : request.prioridad())
                .fechaEstimada(request.fechaEstimada())
                .descripcionProblema(request.descripcionProblema())
                .observaciones(request.observaciones())
                .build();
    }

    public void updateEntity(OrdenServicio orden, OrdenServicioUpdateRequest request) {
        orden.setPrioridad(request.prioridad());
        orden.setFechaEstimada(request.fechaEstimada());
        orden.setDescripcionProblema(request.descripcionProblema());
        orden.setObservaciones(request.observaciones());
    }

    public OrdenServicioResponse toResponse(OrdenServicio orden) {
        Cliente cliente = orden.getCliente();
        Equipo equipo = orden.getEquipo();
        Servicio servicio = orden.getServicio();
        Tecnico tecnico = orden.getTecnico();

        return new OrdenServicioResponse(
                orden.getId(),
                cliente.getId(),
                nombreCliente(cliente),
                equipo.getId(),
                resumenEquipo(equipo),
                servicio.getId(),
                servicio.getNombre(),
                tecnico == null ? null : tecnico.getId(),
                tecnico == null ? null : nombreTecnico(tecnico),
                orden.getEstado(),
                orden.getPrioridad(),
                orden.getFechaIngreso(),
                orden.getFechaEstimada(),
                orden.getDescripcionProblema(),
                orden.getObservaciones(),
                orden.getFechaCreacion(),
                orden.getFechaActualizacion()
        );
    }

    private String nombreCliente(Cliente cliente) {
        return cliente.getApellidos() == null || cliente.getApellidos().isBlank()
                ? cliente.getNombres()
                : cliente.getNombres() + " " + cliente.getApellidos();
    }

    private String resumenEquipo(Equipo equipo) {
        String modelo = equipo.getModelo() == null || equipo.getModelo().isBlank() ? "" : " " + equipo.getModelo();
        return equipo.getTipo() + " " + equipo.getMarca() + modelo;
    }

    private String nombreTecnico(Tecnico tecnico) {
        return tecnico.getNombres() + " " + tecnico.getApellidos();
    }
}
