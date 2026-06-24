package com.nexoservice.pago.service.impl;

import com.nexoservice.cotizacion.entity.Cotizacion;
import com.nexoservice.cotizacion.entity.EstadoCotizacion;
import com.nexoservice.cotizacion.repository.CotizacionRepository;
import com.nexoservice.exception.BusinessException;
import com.nexoservice.exception.ResourceNotFoundException;
import com.nexoservice.orden.entity.EstadoOrden;
import com.nexoservice.orden.entity.OrdenServicio;
import com.nexoservice.orden.repository.OrdenServicioRepository;
import com.nexoservice.pago.dto.request.PagoAnularRequest;
import com.nexoservice.pago.dto.request.PagoCreateRequest;
import com.nexoservice.pago.dto.response.EstadoDeudaResponse;
import com.nexoservice.pago.dto.response.PagoResponse;
import com.nexoservice.pago.entity.EstadoDeuda;
import com.nexoservice.pago.entity.EstadoPago;
import com.nexoservice.pago.entity.Pago;
import com.nexoservice.pago.mapper.PagoMapper;
import com.nexoservice.pago.repository.PagoRepository;
import com.nexoservice.pago.service.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;
    private final OrdenServicioRepository ordenServicioRepository;
    private final CotizacionRepository cotizacionRepository;
    private final PagoMapper pagoMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<PagoResponse> listar(Long ordenServicioId, EstadoPago estado, Pageable pageable) {
        Page<Pago> pagos;

        if (ordenServicioId != null) {
            pagos = pagoRepository.findByOrdenServicioId(ordenServicioId, pageable);
        } else if (estado != null) {
            pagos = pagoRepository.findByEstado(estado, pageable);
        } else {
            pagos = pagoRepository.findAll(pageable);
        }

        return pagos.map(pagoMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public PagoResponse buscarPorId(Long id) {
        return pagoMapper.toResponse(obtenerPago(id));
    }

    @Override
    @Transactional
    public PagoResponse registrar(PagoCreateRequest request) {
        OrdenServicio orden = obtenerOrden(request.ordenServicioId());
        validarOrdenPermitePago(orden);

        Cotizacion cotizacion = resolverCotizacion(request, orden);
        validarMontoContraSaldo(orden, request.monto());

        Pago pago = pagoMapper.toEntity(request, orden, cotizacion);
        return pagoMapper.toResponse(pagoRepository.save(pago));
    }

    @Override
    @Transactional
    public PagoResponse anular(Long id, PagoAnularRequest request) {
        Pago pago = obtenerPago(id);

        if (pago.getEstado() == EstadoPago.ANULADO) {
            throw new BusinessException("El pago ya se encuentra anulado");
        }

        pago.setEstado(EstadoPago.ANULADO);
        pago.setObservaciones(construirObservacionAnulacion(pago.getObservaciones(), request));
        pago.setUsuarioResponsable(request.usuarioResponsable());

        return pagoMapper.toResponse(pago);
    }

    @Override
    @Transactional(readOnly = true)
    public EstadoDeudaResponse consultarDeudaPorOrden(Long ordenServicioId) {
        obtenerOrden(ordenServicioId);
        Optional<Cotizacion> cotizacionAprobada = buscarCotizacionAprobada(ordenServicioId);
        BigDecimal pagado = pagoRepository.sumarPagosRegistradosPorOrden(ordenServicioId);

        if (cotizacionAprobada.isEmpty()) {
            return new EstadoDeudaResponse(
                    ordenServicioId,
                    null,
                    null,
                    BigDecimal.ZERO,
                    pagado,
                    BigDecimal.ZERO,
                    EstadoDeuda.SIN_COTIZACION_APROBADA
            );
        }

        Cotizacion cotizacion = cotizacionAprobada.get();
        BigDecimal total = cotizacion.getTotal();
        BigDecimal saldo = total.subtract(pagado).max(BigDecimal.ZERO);
        EstadoDeuda estado = resolverEstadoDeuda(total, pagado, saldo);

        return new EstadoDeudaResponse(
                ordenServicioId,
                cotizacion.getId(),
                cotizacion.getNumero(),
                total,
                pagado,
                saldo,
                estado
        );
    }

    private Pago obtenerPago(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el pago con id " + id));
    }

    private OrdenServicio obtenerOrden(Long id) {
        return ordenServicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro la orden de servicio con id " + id));
    }

    private Cotizacion resolverCotizacion(PagoCreateRequest request, OrdenServicio orden) {
        Cotizacion cotizacion = request.cotizacionId() == null
                ? buscarCotizacionAprobada(orden.getId()).orElse(null)
                : cotizacionRepository.findById(request.cotizacionId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro la cotizacion con id " + request.cotizacionId()));

        if (cotizacion == null) {
            throw new BusinessException("La orden debe tener una cotizacion aprobada para registrar pagos");
        }

        if (!cotizacion.getOrdenServicio().getId().equals(orden.getId())) {
            throw new BusinessException("La cotizacion no pertenece a la orden indicada");
        }

        if (cotizacion.getEstado() != EstadoCotizacion.APROBADA) {
            throw new BusinessException("Solo se pueden registrar pagos sobre cotizaciones aprobadas");
        }

        return cotizacion;
    }

    private Optional<Cotizacion> buscarCotizacionAprobada(Long ordenServicioId) {
        return cotizacionRepository.findFirstByOrdenServicioIdAndEstadoOrderByIdDesc(ordenServicioId, EstadoCotizacion.APROBADA);
    }

    private void validarOrdenPermitePago(OrdenServicio orden) {
        if (orden.getEstado() == EstadoOrden.CANCELADA) {
            throw new BusinessException("No se puede registrar pagos en una orden cancelada");
        }
    }

    private void validarMontoContraSaldo(OrdenServicio orden, BigDecimal monto) {
        EstadoDeudaResponse deuda = consultarDeudaPorOrden(orden.getId());

        if (deuda.estado() == EstadoDeuda.SIN_COTIZACION_APROBADA) {
            return;
        }

        if (monto.compareTo(deuda.saldo()) > 0) {
            throw new BusinessException("El monto supera el saldo pendiente de la orden");
        }
    }

    private EstadoDeuda resolverEstadoDeuda(BigDecimal total, BigDecimal pagado, BigDecimal saldo) {
        if (pagado.compareTo(BigDecimal.ZERO) == 0) {
            return EstadoDeuda.PENDIENTE;
        }

        if (saldo.compareTo(BigDecimal.ZERO) == 0 || pagado.compareTo(total) >= 0) {
            return EstadoDeuda.PAGADO;
        }

        return EstadoDeuda.PARCIAL;
    }

    private String construirObservacionAnulacion(String observacionesActuales, PagoAnularRequest request) {
        String motivo = "Pago anulado. Motivo: " + request.motivo();
        return observacionesActuales == null || observacionesActuales.isBlank()
                ? motivo
                : observacionesActuales + " | " + motivo;
    }
}
