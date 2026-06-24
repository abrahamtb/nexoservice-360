package com.nexoservice.diagnostico.service;

import com.nexoservice.diagnostico.dto.request.DiagnosticoCreateRequest;
import com.nexoservice.diagnostico.dto.request.DiagnosticoUpdateRequest;
import com.nexoservice.diagnostico.dto.response.DiagnosticoResponse;

import java.util.Optional;

public interface DiagnosticoService {

    DiagnosticoResponse crear(DiagnosticoCreateRequest request);

    DiagnosticoResponse actualizar(Long id, DiagnosticoUpdateRequest request);

    DiagnosticoResponse buscarPorId(Long id);

    DiagnosticoResponse buscarPorOrden(Long ordenServicioId);

    Optional<DiagnosticoResponse> buscarOpcionalPorOrden(Long ordenServicioId);
}
