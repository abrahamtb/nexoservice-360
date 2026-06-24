package com.nexoservice.cotizacion.service;

import com.nexoservice.cotizacion.dto.request.CotizacionCreateRequest;
import com.nexoservice.cotizacion.dto.request.CotizacionEstadoRequest;
import com.nexoservice.cotizacion.dto.request.CotizacionUpdateRequest;
import com.nexoservice.cotizacion.dto.response.CotizacionResponse;
import com.nexoservice.cotizacion.entity.EstadoCotizacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CotizacionService {

    Page<CotizacionResponse> listar(EstadoCotizacion estado, Long ordenServicioId, Pageable pageable);

    CotizacionResponse buscarPorId(Long id);

    CotizacionResponse crear(CotizacionCreateRequest request);

    CotizacionResponse actualizar(Long id, CotizacionUpdateRequest request);

    CotizacionResponse cambiarEstado(Long id, CotizacionEstadoRequest request);
}
