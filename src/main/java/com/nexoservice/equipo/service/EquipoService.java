package com.nexoservice.equipo.service;

import com.nexoservice.equipo.dto.request.EquipoCreateRequest;
import com.nexoservice.equipo.dto.request.EquipoEstadoRequest;
import com.nexoservice.equipo.dto.request.EquipoUpdateRequest;
import com.nexoservice.equipo.dto.response.EquipoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EquipoService {

    Page<EquipoResponse> listar(String buscar, Pageable pageable);

    Page<EquipoResponse> listarPorCliente(Long clienteId, Pageable pageable);

    EquipoResponse buscarPorId(Long id);

    EquipoResponse crear(EquipoCreateRequest request);

    EquipoResponse actualizar(Long id, EquipoUpdateRequest request);

    EquipoResponse cambiarEstado(Long id, EquipoEstadoRequest request);
}
