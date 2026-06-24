package com.nexoservice.trabajoorden.service;

import com.nexoservice.trabajoorden.dto.request.TrabajoOrdenCreateRequest;
import com.nexoservice.trabajoorden.dto.request.TrabajoOrdenUpdateRequest;
import com.nexoservice.trabajoorden.dto.response.TrabajoOrdenResponse;

import java.util.List;

public interface TrabajoOrdenService {

    List<TrabajoOrdenResponse> listarPorOrden(Long ordenServicioId);

    TrabajoOrdenResponse buscarPorId(Long id);

    TrabajoOrdenResponse crear(TrabajoOrdenCreateRequest request);

    TrabajoOrdenResponse actualizar(Long id, TrabajoOrdenUpdateRequest request);
}
