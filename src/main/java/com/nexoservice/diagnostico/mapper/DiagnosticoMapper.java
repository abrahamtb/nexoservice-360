package com.nexoservice.diagnostico.mapper;

import com.nexoservice.diagnostico.dto.request.DiagnosticoCreateRequest;
import com.nexoservice.diagnostico.dto.request.DiagnosticoUpdateRequest;
import com.nexoservice.diagnostico.dto.response.DiagnosticoResponse;
import com.nexoservice.diagnostico.entity.Diagnostico;
import com.nexoservice.orden.entity.OrdenServicio;
import org.springframework.stereotype.Component;

@Component
public class DiagnosticoMapper {

    public Diagnostico toEntity(DiagnosticoCreateRequest request, OrdenServicio ordenServicio) {
        return Diagnostico.builder()
                .ordenServicio(ordenServicio)
                .descripcionFalla(request.descripcionFalla())
                .causaProbable(request.causaProbable())
                .solucionRecomendada(request.solucionRecomendada())
                .observaciones(request.observaciones())
                .usuarioResponsable(request.usuarioResponsable())
                .build();
    }

    public void updateEntity(Diagnostico diagnostico, DiagnosticoUpdateRequest request) {
        diagnostico.setDescripcionFalla(request.descripcionFalla());
        diagnostico.setCausaProbable(request.causaProbable());
        diagnostico.setSolucionRecomendada(request.solucionRecomendada());
        diagnostico.setObservaciones(request.observaciones());
        diagnostico.setUsuarioResponsable(request.usuarioResponsable());
    }

    public DiagnosticoResponse toResponse(Diagnostico diagnostico) {
        return new DiagnosticoResponse(
                diagnostico.getId(),
                diagnostico.getOrdenServicio().getId(),
                diagnostico.getDescripcionFalla(),
                diagnostico.getCausaProbable(),
                diagnostico.getSolucionRecomendada(),
                diagnostico.getObservaciones(),
                diagnostico.getFechaDiagnostico(),
                diagnostico.getUsuarioResponsable(),
                diagnostico.getFechaCreacion(),
                diagnostico.getFechaActualizacion()
        );
    }
}
