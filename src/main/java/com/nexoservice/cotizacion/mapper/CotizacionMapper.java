package com.nexoservice.cotizacion.mapper;

import com.nexoservice.cliente.entity.Cliente;
import com.nexoservice.cotizacion.dto.request.CotizacionCreateRequest;
import com.nexoservice.cotizacion.dto.request.CotizacionDetalleRequest;
import com.nexoservice.cotizacion.dto.response.CotizacionDetalleResponse;
import com.nexoservice.cotizacion.dto.response.CotizacionResponse;
import com.nexoservice.cotizacion.entity.Cotizacion;
import com.nexoservice.cotizacion.entity.CotizacionDetalle;
import com.nexoservice.equipo.entity.Equipo;
import com.nexoservice.orden.entity.OrdenServicio;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CotizacionMapper {

    public Cotizacion toEntity(CotizacionCreateRequest request, OrdenServicio orden) {
        return Cotizacion.builder()
                .ordenServicio(orden)
                .fechaVencimiento(request.fechaVencimiento())
                .observaciones(request.observaciones())
                .build();
    }

    public CotizacionDetalle toDetalle(CotizacionDetalleRequest request) {
        BigDecimal subtotal = request.precioUnitario().multiply(BigDecimal.valueOf(request.cantidad()));

        return CotizacionDetalle.builder()
                .descripcion(request.descripcion())
                .cantidad(request.cantidad())
                .precioUnitario(request.precioUnitario())
                .subtotal(subtotal)
                .build();
    }

    public CotizacionResponse toResponse(Cotizacion cotizacion) {
        OrdenServicio orden = cotizacion.getOrdenServicio();
        Cliente cliente = orden.getCliente();
        Equipo equipo = orden.getEquipo();

        String clienteNombre = cliente.getApellidos() == null || cliente.getApellidos().isBlank()
                ? cliente.getNombres()
                : cliente.getNombres() + " " + cliente.getApellidos();

        String activoResumen = equipo.getTipo() + " " + equipo.getMarca()
                + (equipo.getModelo() == null || equipo.getModelo().isBlank() ? "" : " " + equipo.getModelo());

        return new CotizacionResponse(
                cotizacion.getId(),
                cotizacion.getNumero(),
                orden.getId(),
                clienteNombre,
                activoResumen,
                cotizacion.getFechaEmision(),
                cotizacion.getFechaVencimiento(),
                cotizacion.getEstado(),
                cotizacion.getSubtotal(),
                cotizacion.getImpuesto(),
                cotizacion.getTotal(),
                cotizacion.getObservaciones(),
                toDetallesResponse(cotizacion.getDetalles()),
                cotizacion.getFechaCreacion(),
                cotizacion.getFechaActualizacion()
        );
    }

    private List<CotizacionDetalleResponse> toDetallesResponse(List<CotizacionDetalle> detalles) {
        return detalles.stream()
                .map(detalle -> new CotizacionDetalleResponse(
                        detalle.getId(),
                        detalle.getDescripcion(),
                        detalle.getCantidad(),
                        detalle.getPrecioUnitario(),
                        detalle.getSubtotal()
                ))
                .toList();
    }
}
