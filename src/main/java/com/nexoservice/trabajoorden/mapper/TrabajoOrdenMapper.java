package com.nexoservice.trabajoorden.mapper;

import com.nexoservice.orden.entity.OrdenServicio;
import com.nexoservice.tecnico.entity.Tecnico;
import com.nexoservice.trabajoorden.dto.request.TrabajoOrdenCreateRequest;
import com.nexoservice.trabajoorden.dto.request.TrabajoOrdenUpdateRequest;
import com.nexoservice.trabajoorden.dto.response.TrabajoOrdenResponse;
import com.nexoservice.trabajoorden.entity.TrabajoOrden;
import org.springframework.stereotype.Component;

@Component
public class TrabajoOrdenMapper {

    public TrabajoOrden toEntity(TrabajoOrdenCreateRequest request, OrdenServicio orden, Tecnico tecnico) {
        return TrabajoOrden.builder()
                .ordenServicio(orden)
                .tecnico(tecnico)
                .actividad(request.actividad())
                .detalle(request.detalle())
                .tiempoInvertidoMinutos(request.tiempoInvertidoMinutos())
                .observaciones(request.observaciones())
                .usuarioResponsable(request.usuarioResponsable())
                .build();
    }

    public void updateEntity(TrabajoOrden trabajoOrden, TrabajoOrdenUpdateRequest request, Tecnico tecnico) {
        trabajoOrden.setTecnico(tecnico);
        trabajoOrden.setActividad(request.actividad());
        trabajoOrden.setDetalle(request.detalle());
        trabajoOrden.setTiempoInvertidoMinutos(request.tiempoInvertidoMinutos());
        trabajoOrden.setObservaciones(request.observaciones());
        trabajoOrden.setUsuarioResponsable(request.usuarioResponsable());
    }

    public TrabajoOrdenResponse toResponse(TrabajoOrden trabajoOrden) {
        Tecnico tecnico = trabajoOrden.getTecnico();

        return new TrabajoOrdenResponse(
                trabajoOrden.getId(),
                trabajoOrden.getOrdenServicio().getId(),
                tecnico == null ? null : tecnico.getId(),
                tecnico == null ? null : tecnico.getNombres() + " " + tecnico.getApellidos(),
                trabajoOrden.getActividad(),
                trabajoOrden.getDetalle(),
                trabajoOrden.getFechaTrabajo(),
                trabajoOrden.getTiempoInvertidoMinutos(),
                trabajoOrden.getObservaciones(),
                trabajoOrden.getUsuarioResponsable(),
                trabajoOrden.getFechaCreacion(),
                trabajoOrden.getFechaActualizacion()
        );
    }
}
