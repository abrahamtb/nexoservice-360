package com.nexoservice.cotizacion.service.impl;

import com.nexoservice.cotizacion.dto.request.CotizacionCreateRequest;
import com.nexoservice.cotizacion.dto.request.CotizacionDetalleRequest;
import com.nexoservice.cotizacion.dto.request.CotizacionEstadoRequest;
import com.nexoservice.cotizacion.dto.request.CotizacionUpdateRequest;
import com.nexoservice.cotizacion.dto.response.CotizacionResponse;
import com.nexoservice.cotizacion.entity.Cotizacion;
import com.nexoservice.cotizacion.entity.CotizacionDetalle;
import com.nexoservice.cotizacion.entity.EstadoCotizacion;
import com.nexoservice.cotizacion.mapper.CotizacionMapper;
import com.nexoservice.cotizacion.repository.CotizacionRepository;
import com.nexoservice.cotizacion.service.CotizacionService;
import com.nexoservice.exception.BusinessException;
import com.nexoservice.exception.ResourceNotFoundException;
import com.nexoservice.orden.entity.EstadoOrden;
import com.nexoservice.orden.entity.OrdenServicio;
import com.nexoservice.orden.repository.OrdenServicioRepository;
import com.nexoservice.security.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CotizacionServiceImpl implements CotizacionService {

    private static final BigDecimal IMPUESTO_CERO = BigDecimal.ZERO;

    private final CotizacionRepository cotizacionRepository;
    private final OrdenServicioRepository ordenServicioRepository;
    private final CotizacionMapper cotizacionMapper;
    private final AuthorizationService authorizationService;

    @Override
    @Transactional(readOnly = true)
    public Page<CotizacionResponse> listar(EstadoCotizacion estado, Long ordenServicioId, Pageable pageable) {
        Page<Cotizacion> cotizaciones;

        if (estado != null) {
            cotizaciones = cotizacionRepository.findByEstado(estado, pageable);
        } else if (ordenServicioId != null) {
            OrdenServicio orden = obtenerOrden(ordenServicioId);
            authorizationService.validarOrdenAsignadaAlTecnicoActual(orden);
            cotizaciones = cotizacionRepository.findByOrdenServicioId(ordenServicioId, pageable);
        } else {
            cotizaciones = cotizacionRepository.findAll(pageable);
        }

        return cotizaciones.map(cotizacionMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public CotizacionResponse buscarPorId(Long id) {
        Cotizacion cotizacion = obtenerCotizacion(id);
        authorizationService.validarOrdenAsignadaAlTecnicoActual(cotizacion.getOrdenServicio());
        return cotizacionMapper.toResponse(cotizacion);
    }

    @Override
    @Transactional
    public CotizacionResponse crear(CotizacionCreateRequest request) {
        OrdenServicio orden = obtenerOrden(request.ordenServicioId());
        authorizationService.validarOrdenAsignadaAlTecnicoActual(orden);
        validarOrdenPermiteCotizacion(orden);

        Cotizacion cotizacion = cotizacionMapper.toEntity(request, orden);
        reemplazarDetalles(cotizacion, request.detalles());
        recalcularTotales(cotizacion);

        Cotizacion cotizacionGuardada = cotizacionRepository.save(cotizacion);
        cotizacionGuardada.setNumero(generarNumero(cotizacionGuardada.getId()));

        return cotizacionMapper.toResponse(cotizacionGuardada);
    }

    @Override
    @Transactional
    public CotizacionResponse actualizar(Long id, CotizacionUpdateRequest request) {
        Cotizacion cotizacion = obtenerCotizacion(id);
        authorizationService.validarOrdenAsignadaAlTecnicoActual(cotizacion.getOrdenServicio());
        validarCotizacionEditable(cotizacion);

        cotizacion.setFechaVencimiento(request.fechaVencimiento());
        cotizacion.setObservaciones(request.observaciones());
        cotizacion.limpiarDetalles();
        reemplazarDetalles(cotizacion, request.detalles());
        recalcularTotales(cotizacion);

        return cotizacionMapper.toResponse(cotizacion);
    }

    @Override
    @Transactional
    public CotizacionResponse cambiarEstado(Long id, CotizacionEstadoRequest request) {
        Cotizacion cotizacion = obtenerCotizacion(id);
        authorizationService.validarOrdenAsignadaAlTecnicoActual(cotizacion.getOrdenServicio());
        cotizacion.setEstado(request.estado());
        return cotizacionMapper.toResponse(cotizacion);
    }

    private Cotizacion obtenerCotizacion(Long id) {
        return cotizacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro la cotizacion con id " + id));
    }

    private OrdenServicio obtenerOrden(Long id) {
        return ordenServicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro la orden de servicio con id " + id));
    }

    private void reemplazarDetalles(Cotizacion cotizacion, java.util.List<CotizacionDetalleRequest> detalles) {
        for (CotizacionDetalleRequest detalleRequest : detalles) {
            CotizacionDetalle detalle = cotizacionMapper.toDetalle(detalleRequest);
            cotizacion.agregarDetalle(detalle);
        }
    }

    private void recalcularTotales(Cotizacion cotizacion) {
        BigDecimal subtotal = cotizacion.getDetalles().stream()
                .map(CotizacionDetalle::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cotizacion.setSubtotal(subtotal);
        cotizacion.setImpuesto(IMPUESTO_CERO);
        cotizacion.setTotal(subtotal.add(IMPUESTO_CERO));
    }

    private void validarOrdenPermiteCotizacion(OrdenServicio orden) {
        if (orden.getEstado() == EstadoOrden.CANCELADA || orden.getEstado() == EstadoOrden.ENTREGADA) {
            throw new BusinessException("No se puede cotizar una orden cancelada o entregada");
        }
    }

    private void validarCotizacionEditable(Cotizacion cotizacion) {
        if (cotizacion.getEstado() != EstadoCotizacion.PENDIENTE) {
            throw new BusinessException("Solo se puede editar una cotizacion pendiente");
        }
    }

    private String generarNumero(Long id) {
        return "COT-" + String.format("%06d", id);
    }
}
