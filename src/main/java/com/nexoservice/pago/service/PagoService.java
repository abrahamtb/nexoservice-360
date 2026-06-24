package com.nexoservice.pago.service;

import com.nexoservice.pago.dto.request.PagoAnularRequest;
import com.nexoservice.pago.dto.request.PagoCreateRequest;
import com.nexoservice.pago.dto.response.EstadoDeudaResponse;
import com.nexoservice.pago.dto.response.PagoResponse;
import com.nexoservice.pago.entity.EstadoPago;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PagoService {

    Page<PagoResponse> listar(Long ordenServicioId, EstadoPago estado, Pageable pageable);

    PagoResponse buscarPorId(Long id);

    PagoResponse registrar(PagoCreateRequest request);

    PagoResponse anular(Long id, PagoAnularRequest request);

    EstadoDeudaResponse consultarDeudaPorOrden(Long ordenServicioId);
}
