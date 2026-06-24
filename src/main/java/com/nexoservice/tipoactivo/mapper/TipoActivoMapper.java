package com.nexoservice.tipoactivo.mapper;

import com.nexoservice.tipoactivo.dto.request.TipoActivoCreateRequest;
import com.nexoservice.tipoactivo.dto.request.TipoActivoUpdateRequest;
import com.nexoservice.tipoactivo.dto.response.TipoActivoResponse;
import com.nexoservice.tipoactivo.entity.TipoActivo;
import org.springframework.stereotype.Component;

@Component
public class TipoActivoMapper {

    public TipoActivo toEntity(TipoActivoCreateRequest request) {
        return TipoActivo.builder()
                .nombre(normalizarNombre(request.nombre()))
                .descripcion(request.descripcion())
                .build();
    }

    public void updateEntity(TipoActivo tipoActivo, TipoActivoUpdateRequest request) {
        tipoActivo.setNombre(normalizarNombre(request.nombre()));
        tipoActivo.setDescripcion(request.descripcion());
    }

    public TipoActivoResponse toResponse(TipoActivo tipoActivo) {
        return new TipoActivoResponse(
                tipoActivo.getId(),
                tipoActivo.getNombre(),
                tipoActivo.getDescripcion(),
                tipoActivo.getActivo(),
                tipoActivo.getFechaCreacion(),
                tipoActivo.getFechaActualizacion()
        );
    }

    private String normalizarNombre(String nombre) {
        return nombre == null ? null : nombre.trim();
    }
}
