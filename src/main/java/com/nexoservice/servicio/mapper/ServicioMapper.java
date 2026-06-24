package com.nexoservice.servicio.mapper;

import com.nexoservice.servicio.dto.request.ServicioCreateRequest;
import com.nexoservice.servicio.dto.request.ServicioUpdateRequest;
import com.nexoservice.servicio.dto.response.ServicioResponse;
import com.nexoservice.servicio.entity.Servicio;
import org.springframework.stereotype.Component;

@Component
public class ServicioMapper {

    public Servicio toEntity(ServicioCreateRequest request) {
        return Servicio.builder()
                .nombre(normalizarNombre(request.nombre()))
                .descripcion(request.descripcion())
                .precioBase(request.precioBase())
                .duracionEstimadaMinutos(request.duracionEstimadaMinutos())
                .activo(true)
                .build();
    }

    public void updateEntity(Servicio servicio, ServicioUpdateRequest request) {
        servicio.setNombre(normalizarNombre(request.nombre()));
        servicio.setDescripcion(request.descripcion());
        servicio.setPrecioBase(request.precioBase());
        servicio.setDuracionEstimadaMinutos(request.duracionEstimadaMinutos());
    }

    public ServicioResponse toResponse(Servicio servicio) {
        return new ServicioResponse(
                servicio.getId(),
                servicio.getNombre(),
                servicio.getDescripcion(),
                servicio.getPrecioBase(),
                servicio.getDuracionEstimadaMinutos(),
                servicio.getActivo(),
                servicio.getFechaCreacion(),
                servicio.getFechaActualizacion()
        );
    }

    private String normalizarNombre(String nombre) {
        return nombre == null ? null : nombre.trim();
    }
}
