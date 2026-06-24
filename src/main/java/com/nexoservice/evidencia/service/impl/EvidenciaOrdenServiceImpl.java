package com.nexoservice.evidencia.service.impl;

import com.nexoservice.evidencia.dto.request.EvidenciaOrdenCreateRequest;
import com.nexoservice.evidencia.dto.response.EvidenciaOrdenResponse;
import com.nexoservice.evidencia.mapper.EvidenciaOrdenMapper;
import com.nexoservice.evidencia.repository.EvidenciaOrdenRepository;
import com.nexoservice.evidencia.service.EvidenciaOrdenService;
import com.nexoservice.exception.ResourceNotFoundException;
import com.nexoservice.orden.entity.OrdenServicio;
import com.nexoservice.orden.repository.OrdenServicioRepository;
import com.nexoservice.security.AuthorizationService;
import com.nexoservice.tecnico.entity.Tecnico;
import com.nexoservice.tecnico.repository.TecnicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EvidenciaOrdenServiceImpl implements EvidenciaOrdenService {

    private final EvidenciaOrdenRepository evidenciaRepository;
    private final OrdenServicioRepository ordenServicioRepository;
    private final TecnicoRepository tecnicoRepository;
    private final EvidenciaOrdenMapper mapper;
    private final AuthorizationService authorizationService;

    @Override
    @Transactional(readOnly = true)
    public List<EvidenciaOrdenResponse> listarPorOrden(Long ordenServicioId) {
        OrdenServicio orden = obtenerOrden(ordenServicioId);
        authorizationService.validarOrdenAsignadaAlTecnicoActual(orden);
        return evidenciaRepository.findByOrdenServicioIdOrderByFechaRegistroDesc(ordenServicioId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public EvidenciaOrdenResponse crear(EvidenciaOrdenCreateRequest request) {
        OrdenServicio orden = obtenerOrden(request.ordenServicioId());
        authorizationService.validarOrdenAsignadaAlTecnicoActual(orden);
        Tecnico tecnico = request.tecnicoId() == null ? null : tecnicoRepository.findById(request.tecnicoId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el tecnico con id " + request.tecnicoId()));
        return mapper.toResponse(evidenciaRepository.save(mapper.toEntity(request, orden, tecnico)));
    }

    private OrdenServicio obtenerOrden(Long id) {
        return ordenServicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro la orden de servicio con id " + id));
    }
}
