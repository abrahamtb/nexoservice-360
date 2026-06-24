package com.nexoservice.evidencia.mapper;

import com.nexoservice.evidencia.dto.request.EvidenciaOrdenCreateRequest;
import com.nexoservice.evidencia.dto.response.EvidenciaOrdenResponse;
import com.nexoservice.evidencia.entity.EvidenciaOrden;
import com.nexoservice.orden.entity.OrdenServicio;
import com.nexoservice.tecnico.entity.Tecnico;
import org.springframework.stereotype.Component;

@Component
public class EvidenciaOrdenMapper {

    public EvidenciaOrden toEntity(EvidenciaOrdenCreateRequest request, OrdenServicio orden, Tecnico tecnico) {
        return EvidenciaOrden.builder()
                .ordenServicio(orden)
                .tecnico(tecnico)
                .tipo(request.tipo())
                .nombreArchivo(normalizar(request.nombreArchivo()))
                .urlArchivo(normalizar(request.urlArchivo()))
                .observaciones(normalizar(request.observaciones()))
                .usuarioResponsable(normalizar(request.usuarioResponsable()))
                .build();
    }

    public EvidenciaOrdenResponse toResponse(EvidenciaOrden evidencia) {
        Tecnico tecnico = evidencia.getTecnico();
        return new EvidenciaOrdenResponse(
                evidencia.getId(),
                evidencia.getOrdenServicio().getId(),
                tecnico == null ? null : tecnico.getId(),
                tecnico == null ? null : tecnico.getNombres() + " " + tecnico.getApellidos(),
                evidencia.getTipo(),
                evidencia.getNombreArchivo(),
                evidencia.getUrlArchivo(),
                evidencia.getObservaciones(),
                evidencia.getUsuarioResponsable(),
                evidencia.getFechaRegistro()
        );
    }

    private String normalizar(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }
}
