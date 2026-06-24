package com.nexoservice.tecnico.mapper;

import com.nexoservice.tecnico.dto.request.TecnicoCreateRequest;
import com.nexoservice.tecnico.dto.request.TecnicoUpdateRequest;
import com.nexoservice.tecnico.dto.response.TecnicoResponse;
import com.nexoservice.tecnico.entity.Tecnico;
import org.springframework.stereotype.Component;

@Component
public class TecnicoMapper {

    public Tecnico toEntity(TecnicoCreateRequest request) {
        return Tecnico.builder()
                .nombres(request.nombres())
                .apellidos(request.apellidos())
                .especialidad(request.especialidad())
                .correo(normalizarCorreo(request.correo()))
                .telefono(request.telefono())
                .activo(true)
                .build();
    }

    public void updateEntity(Tecnico tecnico, TecnicoUpdateRequest request) {
        tecnico.setNombres(request.nombres());
        tecnico.setApellidos(request.apellidos());
        tecnico.setEspecialidad(request.especialidad());
        tecnico.setCorreo(normalizarCorreo(request.correo()));
        tecnico.setTelefono(request.telefono());
    }

    public TecnicoResponse toResponse(Tecnico tecnico) {
        return new TecnicoResponse(
                tecnico.getId(),
                tecnico.getNombres(),
                tecnico.getApellidos(),
                tecnico.getEspecialidad(),
                tecnico.getCorreo(),
                tecnico.getTelefono(),
                tecnico.getDisponibilidad(),
                tecnico.getActivo(),
                tecnico.getFechaCreacion(),
                tecnico.getFechaActualizacion()
        );
    }

    private String normalizarCorreo(String correo) {
        return correo == null || correo.isBlank() ? null : correo.trim().toLowerCase();
    }
}
