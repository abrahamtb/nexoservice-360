package com.nexoservice.tecnico.service;

import com.nexoservice.tecnico.dto.request.TecnicoCreateRequest;
import com.nexoservice.tecnico.dto.request.TecnicoDisponibilidadRequest;
import com.nexoservice.tecnico.dto.request.TecnicoEstadoRequest;
import com.nexoservice.tecnico.dto.request.TecnicoUpdateRequest;
import com.nexoservice.tecnico.dto.response.TecnicoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TecnicoService {

    Page<TecnicoResponse> listar(String buscar, Pageable pageable);

    Page<TecnicoResponse> listarDisponibles(Pageable pageable);

    TecnicoResponse buscarPorId(Long id);

    TecnicoResponse crear(TecnicoCreateRequest request);

    TecnicoResponse actualizar(Long id, TecnicoUpdateRequest request);

    TecnicoResponse cambiarEstado(Long id, TecnicoEstadoRequest request);

    TecnicoResponse cambiarDisponibilidad(Long id, TecnicoDisponibilidadRequest request);
}
