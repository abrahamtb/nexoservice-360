package com.nexoservice.pago.mapper;

import com.nexoservice.cotizacion.entity.Cotizacion;
import com.nexoservice.orden.entity.OrdenServicio;
import com.nexoservice.pago.dto.request.PagoCreateRequest;
import com.nexoservice.pago.dto.response.PagoResponse;
import com.nexoservice.pago.entity.Pago;
import org.springframework.stereotype.Component;

@Component
public class PagoMapper {

    public Pago toEntity(PagoCreateRequest request, OrdenServicio orden, Cotizacion cotizacion) {
        return Pago.builder()
                .ordenServicio(orden)
                .cotizacion(cotizacion)
                .monto(request.monto())
                .metodoPago(request.metodoPago())
                .numeroOperacion(normalizar(request.numeroOperacion()))
                .observaciones(normalizar(request.observaciones()))
                .usuarioResponsable(normalizar(request.usuarioResponsable()))
                .build();
    }

    public PagoResponse toResponse(Pago pago) {
        Cotizacion cotizacion = pago.getCotizacion();

        return new PagoResponse(
                pago.getId(),
                pago.getOrdenServicio().getId(),
                cotizacion == null ? null : cotizacion.getId(),
                cotizacion == null ? null : cotizacion.getNumero(),
                pago.getMonto(),
                pago.getMetodoPago(),
                pago.getEstado(),
                pago.getNumeroOperacion(),
                pago.getObservaciones(),
                pago.getUsuarioResponsable(),
                pago.getFechaPago(),
                pago.getFechaCreacion(),
                pago.getFechaActualizacion()
        );
    }

    private String normalizar(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }
}
