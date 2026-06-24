package com.nexoservice.historial.mapper;

import com.nexoservice.historial.dto.response.HistorialOrdenResponse;
import com.nexoservice.historial.entity.HistorialOrden;
import org.springframework.stereotype.Component;

@Component
public class HistorialOrdenMapper {

    public HistorialOrdenResponse toResponse(HistorialOrden historial) {
        return new HistorialOrdenResponse(
                historial.getId(),
                historial.getOrdenServicio().getId(),
                historial.getEstadoAnterior(),
                historial.getEstadoNuevo(),
                historial.getFechaCambio(),
                historial.getUsuarioResponsable(),
                historial.getObservacion()
        );
    }
}
