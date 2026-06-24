package com.nexoservice.servicio.service;

import com.nexoservice.servicio.dto.request.ServicioCreateRequest;
import com.nexoservice.servicio.dto.request.ServicioEstadoRequest;
import com.nexoservice.servicio.dto.request.ServicioUpdateRequest;
import com.nexoservice.servicio.dto.response.ServicioResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ServicioService {

    Page<ServicioResponse> listar(String buscar, Pageable pageable);

    Page<ServicioResponse> listarActivos(Pageable pageable);

    ServicioResponse buscarPorId(Long id);

    ServicioResponse crear(ServicioCreateRequest request);

    ServicioResponse actualizar(Long id, ServicioUpdateRequest request);

    ServicioResponse cambiarEstado(Long id, ServicioEstadoRequest request);
}
