package com.nexoservice.evidencia.service;

import com.nexoservice.evidencia.dto.request.EvidenciaOrdenCreateRequest;
import com.nexoservice.evidencia.dto.response.EvidenciaOrdenResponse;

import java.util.List;

public interface EvidenciaOrdenService {

    List<EvidenciaOrdenResponse> listarPorOrden(Long ordenServicioId);

    EvidenciaOrdenResponse crear(EvidenciaOrdenCreateRequest request);
}
