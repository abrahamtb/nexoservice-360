package com.nexoservice.tipoactivo.service;

import com.nexoservice.tipoactivo.dto.request.TipoActivoCreateRequest;
import com.nexoservice.tipoactivo.dto.request.TipoActivoEstadoRequest;
import com.nexoservice.tipoactivo.dto.request.TipoActivoUpdateRequest;
import com.nexoservice.tipoactivo.dto.response.TipoActivoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TipoActivoService {

    Page<TipoActivoResponse> listar(String buscar, Pageable pageable);

    Page<TipoActivoResponse> listarActivos(Pageable pageable);

    TipoActivoResponse buscarPorId(Long id);

    TipoActivoResponse crear(TipoActivoCreateRequest request);

    TipoActivoResponse actualizar(Long id, TipoActivoUpdateRequest request);

    TipoActivoResponse cambiarEstado(Long id, TipoActivoEstadoRequest request);
}
