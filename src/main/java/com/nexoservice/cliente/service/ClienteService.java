package com.nexoservice.cliente.service;

import com.nexoservice.cliente.dto.request.ClienteCreateRequest;
import com.nexoservice.cliente.dto.request.ClienteEstadoRequest;
import com.nexoservice.cliente.dto.request.ClienteUpdateRequest;
import com.nexoservice.cliente.dto.response.ClienteResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClienteService {

    Page<ClienteResponse> listar(String buscar, Pageable pageable);

    ClienteResponse buscarPorId(Long id);

    ClienteResponse crear(ClienteCreateRequest request);

    ClienteResponse actualizar(Long id, ClienteUpdateRequest request);

    ClienteResponse cambiarEstado(Long id, ClienteEstadoRequest request);
}
