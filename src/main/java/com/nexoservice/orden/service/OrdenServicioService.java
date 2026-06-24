package com.nexoservice.orden.service;

import com.nexoservice.historial.dto.response.HistorialOrdenResponse;
import com.nexoservice.orden.dto.request.AsignarTecnicoRequest;
import com.nexoservice.orden.dto.request.CambiarEstadoOrdenRequest;
import com.nexoservice.orden.dto.request.OrdenServicioCreateRequest;
import com.nexoservice.orden.dto.request.OrdenServicioUpdateRequest;
import com.nexoservice.orden.dto.response.OrdenServicioResponse;
import com.nexoservice.orden.entity.EstadoOrden;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrdenServicioService {

    Page<OrdenServicioResponse> listar(EstadoOrden estado, Long clienteId, Long tecnicoId, Pageable pageable);

    OrdenServicioResponse buscarPorId(Long id);

    OrdenServicioResponse crear(OrdenServicioCreateRequest request);

    OrdenServicioResponse actualizar(Long id, OrdenServicioUpdateRequest request);

    OrdenServicioResponse asignarTecnico(Long id, AsignarTecnicoRequest request);

    OrdenServicioResponse cambiarEstado(Long id, CambiarEstadoOrdenRequest request);

    List<HistorialOrdenResponse> listarHistorial(Long id);
}
