package com.nexoservice.mantenimiento.service.impl;

import com.nexoservice.equipo.entity.Equipo;
import com.nexoservice.equipo.repository.EquipoRepository;
import com.nexoservice.exception.BusinessException;
import com.nexoservice.exception.ResourceNotFoundException;
import com.nexoservice.mantenimiento.dto.request.MantenimientoProgramadoCreateRequest;
import com.nexoservice.mantenimiento.dto.response.MantenimientoProgramadoResponse;
import com.nexoservice.mantenimiento.entity.EstadoMantenimientoProgramado;
import com.nexoservice.mantenimiento.entity.MantenimientoProgramado;
import com.nexoservice.mantenimiento.mapper.MantenimientoProgramadoMapper;
import com.nexoservice.mantenimiento.repository.MantenimientoProgramadoRepository;
import com.nexoservice.mantenimiento.service.MantenimientoProgramadoService;
import com.nexoservice.orden.dto.request.OrdenServicioCreateRequest;
import com.nexoservice.orden.dto.response.OrdenServicioResponse;
import com.nexoservice.orden.entity.PrioridadOrden;
import com.nexoservice.orden.service.OrdenServicioService;
import com.nexoservice.servicio.entity.Servicio;
import com.nexoservice.servicio.repository.ServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MantenimientoProgramadoServiceImpl implements MantenimientoProgramadoService {

    private final MantenimientoProgramadoRepository mantenimientoRepository;
    private final EquipoRepository equipoRepository;
    private final ServicioRepository servicioRepository;
    private final OrdenServicioService ordenServicioService;
    private final MantenimientoProgramadoMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<MantenimientoProgramadoResponse> listar(EstadoMantenimientoProgramado estado, boolean soloAlertas, Pageable pageable) {
        Page<MantenimientoProgramado> mantenimientos;

        if (soloAlertas) {
            mantenimientos = mantenimientoRepository.findByEstadoAndProximaFechaLessThanEqual(
                    EstadoMantenimientoProgramado.ACTIVO,
                    LocalDate.now().plusDays(7),
                    pageable
            );
        } else if (estado != null) {
            mantenimientos = mantenimientoRepository.findByEstado(estado, pageable);
        } else {
            mantenimientos = mantenimientoRepository.findAll(pageable);
        }

        return mantenimientos.map(mapper::toResponse);
    }

    @Override
    @Transactional
    public MantenimientoProgramadoResponse crear(MantenimientoProgramadoCreateRequest request) {
        Equipo equipo = equipoRepository.findById(request.equipoId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el activo con id " + request.equipoId()));
        Servicio servicio = servicioRepository.findById(request.servicioId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el servicio con id " + request.servicioId()));

        return mapper.toResponse(mantenimientoRepository.save(mapper.toEntity(request, equipo, servicio)));
    }

    @Override
    @Transactional
    public OrdenServicioResponse generarOrden(Long id, String usuarioResponsable) {
        MantenimientoProgramado mantenimiento = mantenimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el mantenimiento programado con id " + id));

        if (mantenimiento.getEstado() != EstadoMantenimientoProgramado.ACTIVO) {
            throw new BusinessException("Solo se puede generar ordenes desde mantenimientos activos");
        }

        Equipo equipo = mantenimiento.getEquipo();
        OrdenServicioResponse orden = ordenServicioService.crear(new OrdenServicioCreateRequest(
                equipo.getCliente().getId(),
                equipo.getId(),
                mantenimiento.getServicio().getId(),
                null,
                PrioridadOrden.MEDIA,
                mantenimiento.getProximaFecha(),
                "Mantenimiento preventivo programado: " + mantenimiento.getServicio().getNombre(),
                mantenimiento.getObservaciones(),
                usuarioResponsable
        ));

        mantenimiento.setUltimaOrdenId(orden.id());
        mantenimiento.setProximaFecha(mantenimiento.getProximaFecha().plusMonths(mantenimiento.getFrecuencia().getMeses()));

        return orden;
    }
}
