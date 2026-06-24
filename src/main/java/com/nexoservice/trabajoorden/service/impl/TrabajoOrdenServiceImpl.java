package com.nexoservice.trabajoorden.service.impl;

import com.nexoservice.exception.BusinessException;
import com.nexoservice.exception.ResourceNotFoundException;
import com.nexoservice.orden.entity.EstadoOrden;
import com.nexoservice.orden.entity.OrdenServicio;
import com.nexoservice.orden.repository.OrdenServicioRepository;
import com.nexoservice.security.AuthorizationService;
import com.nexoservice.tecnico.entity.Tecnico;
import com.nexoservice.tecnico.repository.TecnicoRepository;
import com.nexoservice.trabajoorden.dto.request.TrabajoOrdenCreateRequest;
import com.nexoservice.trabajoorden.dto.request.TrabajoOrdenUpdateRequest;
import com.nexoservice.trabajoorden.dto.response.TrabajoOrdenResponse;
import com.nexoservice.trabajoorden.entity.TrabajoOrden;
import com.nexoservice.trabajoorden.mapper.TrabajoOrdenMapper;
import com.nexoservice.trabajoorden.repository.TrabajoOrdenRepository;
import com.nexoservice.trabajoorden.service.TrabajoOrdenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrabajoOrdenServiceImpl implements TrabajoOrdenService {

    private final TrabajoOrdenRepository trabajoOrdenRepository;
    private final OrdenServicioRepository ordenServicioRepository;
    private final TecnicoRepository tecnicoRepository;
    private final TrabajoOrdenMapper trabajoOrdenMapper;
    private final AuthorizationService authorizationService;

    @Override
    @Transactional(readOnly = true)
    public List<TrabajoOrdenResponse> listarPorOrden(Long ordenServicioId) {
        OrdenServicio orden = obtenerOrden(ordenServicioId);
        authorizationService.validarOrdenAsignadaAlTecnicoActual(orden);

        return trabajoOrdenRepository.findByOrdenServicioIdOrderByFechaTrabajoAsc(ordenServicioId)
                .stream()
                .map(trabajoOrdenMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TrabajoOrdenResponse buscarPorId(Long id) {
        TrabajoOrden trabajoOrden = obtenerTrabajo(id);
        authorizationService.validarOrdenAsignadaAlTecnicoActual(trabajoOrden.getOrdenServicio());
        return trabajoOrdenMapper.toResponse(trabajoOrden);
    }

    @Override
    @Transactional
    public TrabajoOrdenResponse crear(TrabajoOrdenCreateRequest request) {
        OrdenServicio orden = obtenerOrden(request.ordenServicioId());
        authorizationService.validarOrdenAsignadaAlTecnicoActual(orden);
        validarOrdenPermiteTrabajo(orden);

        Tecnico tecnico = resolverTecnico(request.tecnicoId(), orden);
        TrabajoOrden trabajoOrden = trabajoOrdenMapper.toEntity(request, orden, tecnico);
        TrabajoOrden trabajoGuardado = trabajoOrdenRepository.save(trabajoOrden);

        return trabajoOrdenMapper.toResponse(trabajoGuardado);
    }

    @Override
    @Transactional
    public TrabajoOrdenResponse actualizar(Long id, TrabajoOrdenUpdateRequest request) {
        TrabajoOrden trabajoOrden = obtenerTrabajo(id);
        OrdenServicio orden = trabajoOrden.getOrdenServicio();
        authorizationService.validarOrdenAsignadaAlTecnicoActual(orden);
        validarOrdenPermiteTrabajo(orden);

        Tecnico tecnico = resolverTecnico(request.tecnicoId(), orden);
        trabajoOrdenMapper.updateEntity(trabajoOrden, request, tecnico);

        return trabajoOrdenMapper.toResponse(trabajoOrden);
    }

    private TrabajoOrden obtenerTrabajo(Long id) {
        return trabajoOrdenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el trabajo con id " + id));
    }

    private OrdenServicio obtenerOrden(Long id) {
        return ordenServicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro la orden de servicio con id " + id));
    }

    private Tecnico obtenerTecnico(Long id) {
        return tecnicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el tecnico con id " + id));
    }

    private Tecnico resolverTecnico(Long tecnicoId, OrdenServicio orden) {
        if (tecnicoId != null) {
            Tecnico tecnico = obtenerTecnico(tecnicoId);

            if (orden.getTecnico() != null && !orden.getTecnico().getId().equals(tecnico.getId()) && authorizationService.esTecnico()) {
                throw new BusinessException("El tecnico solo puede registrar trabajos con su propio tecnico asignado");
            }

            return tecnico;
        }

        return orden.getTecnico();
    }

    private void validarOrdenPermiteTrabajo(OrdenServicio orden) {
        if (orden.getEstado() == EstadoOrden.CANCELADA || orden.getEstado() == EstadoOrden.ENTREGADA) {
            throw new BusinessException("No se puede registrar trabajos en una orden cancelada o entregada");
        }

        if (orden.getTecnico() == null) {
            throw new BusinessException("La orden necesita un tecnico asignado para registrar trabajos");
        }
    }
}
