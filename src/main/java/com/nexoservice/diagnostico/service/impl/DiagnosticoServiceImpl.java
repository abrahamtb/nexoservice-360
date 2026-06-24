package com.nexoservice.diagnostico.service.impl;

import com.nexoservice.diagnostico.dto.request.DiagnosticoCreateRequest;
import com.nexoservice.diagnostico.dto.request.DiagnosticoUpdateRequest;
import com.nexoservice.diagnostico.dto.response.DiagnosticoResponse;
import com.nexoservice.diagnostico.entity.Diagnostico;
import com.nexoservice.diagnostico.mapper.DiagnosticoMapper;
import com.nexoservice.diagnostico.repository.DiagnosticoRepository;
import com.nexoservice.diagnostico.service.DiagnosticoService;
import com.nexoservice.exception.BusinessException;
import com.nexoservice.exception.DuplicateResourceException;
import com.nexoservice.exception.ResourceNotFoundException;
import com.nexoservice.historial.entity.HistorialOrden;
import com.nexoservice.historial.repository.HistorialOrdenRepository;
import com.nexoservice.orden.entity.EstadoOrden;
import com.nexoservice.orden.entity.OrdenServicio;
import com.nexoservice.orden.repository.OrdenServicioRepository;
import com.nexoservice.security.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiagnosticoServiceImpl implements DiagnosticoService {

    private final DiagnosticoRepository diagnosticoRepository;
    private final OrdenServicioRepository ordenServicioRepository;
    private final HistorialOrdenRepository historialOrdenRepository;
    private final DiagnosticoMapper diagnosticoMapper;
    private final AuthorizationService authorizationService;

    @Override
    @Transactional
    public DiagnosticoResponse crear(DiagnosticoCreateRequest request) {
        OrdenServicio orden = obtenerOrden(request.ordenServicioId());
        authorizationService.validarOrdenAsignadaAlTecnicoActual(orden);
        validarOrdenPermiteDiagnostico(orden);
        validarOrdenListaParaDiagnostico(orden);
        validarDiagnosticoNoDuplicado(orden.getId());

        Diagnostico diagnostico = diagnosticoMapper.toEntity(request, orden);
        Diagnostico diagnosticoGuardado = diagnosticoRepository.save(diagnostico);

        orden.setEstado(EstadoOrden.ESPERANDO_APROBACION);
        registrarHistorialDiagnostico(orden, request.usuarioResponsable());

        return diagnosticoMapper.toResponse(diagnosticoGuardado);
    }

    @Override
    @Transactional
    public DiagnosticoResponse actualizar(Long id, DiagnosticoUpdateRequest request) {
        Diagnostico diagnostico = obtenerDiagnostico(id);
        authorizationService.validarOrdenAsignadaAlTecnicoActual(diagnostico.getOrdenServicio());
        validarOrdenPermiteDiagnostico(diagnostico.getOrdenServicio());

        diagnosticoMapper.updateEntity(diagnostico, request);

        return diagnosticoMapper.toResponse(diagnostico);
    }

    @Override
    @Transactional(readOnly = true)
    public DiagnosticoResponse buscarPorId(Long id) {
        Diagnostico diagnostico = obtenerDiagnostico(id);
        authorizationService.validarOrdenAsignadaAlTecnicoActual(diagnostico.getOrdenServicio());
        return diagnosticoMapper.toResponse(diagnostico);
    }

    @Override
    @Transactional(readOnly = true)
    public DiagnosticoResponse buscarPorOrden(Long ordenServicioId) {
        OrdenServicio orden = obtenerOrden(ordenServicioId);
        authorizationService.validarOrdenAsignadaAlTecnicoActual(orden);

        return diagnosticoRepository.findByOrdenServicioId(ordenServicioId)
                .map(diagnosticoMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro diagnostico para la orden " + ordenServicioId));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DiagnosticoResponse> buscarOpcionalPorOrden(Long ordenServicioId) {
        OrdenServicio orden = obtenerOrden(ordenServicioId);
        authorizationService.validarOrdenAsignadaAlTecnicoActual(orden);

        return diagnosticoRepository.findByOrdenServicioId(ordenServicioId)
                .map(diagnosticoMapper::toResponse);
    }

    private Diagnostico obtenerDiagnostico(Long id) {
        return diagnosticoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el diagnostico con id " + id));
    }

    private OrdenServicio obtenerOrden(Long id) {
        return ordenServicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro la orden de servicio con id " + id));
    }

    private void validarDiagnosticoNoDuplicado(Long ordenServicioId) {
        if (diagnosticoRepository.existsByOrdenServicioId(ordenServicioId)) {
            throw new DuplicateResourceException("La orden ya tiene un diagnostico registrado");
        }
    }

    private void validarOrdenPermiteDiagnostico(OrdenServicio orden) {
        if (orden.getEstado() == EstadoOrden.CANCELADA || orden.getEstado() == EstadoOrden.ENTREGADA) {
            throw new BusinessException("No se puede registrar o editar diagnostico en una orden cancelada o entregada");
        }

        if (orden.getTecnico() == null) {
            throw new BusinessException("La orden necesita un tecnico asignado para registrar diagnostico");
        }
    }

    private void validarOrdenListaParaDiagnostico(OrdenServicio orden) {
        if (orden.getEstado() != EstadoOrden.EN_DIAGNOSTICO) {
            throw new BusinessException("La orden debe estar en estado EN_DIAGNOSTICO para registrar el diagnostico");
        }
    }

    private void registrarHistorialDiagnostico(OrdenServicio orden, String usuarioResponsable) {
        HistorialOrden historial = HistorialOrden.builder()
                .ordenServicio(orden)
                .estadoAnterior(EstadoOrden.EN_DIAGNOSTICO)
                .estadoNuevo(EstadoOrden.ESPERANDO_APROBACION)
                .fechaCambio(LocalDateTime.now())
                .usuarioResponsable(usuarioResponsable)
                .observacion("Diagnostico registrado")
                .build();

        historialOrdenRepository.save(historial);
    }
}
