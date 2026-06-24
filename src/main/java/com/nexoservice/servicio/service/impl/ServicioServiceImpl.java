package com.nexoservice.servicio.service.impl;

import com.nexoservice.exception.DuplicateResourceException;
import com.nexoservice.exception.ResourceNotFoundException;
import com.nexoservice.servicio.dto.request.ServicioCreateRequest;
import com.nexoservice.servicio.dto.request.ServicioEstadoRequest;
import com.nexoservice.servicio.dto.request.ServicioUpdateRequest;
import com.nexoservice.servicio.dto.response.ServicioResponse;
import com.nexoservice.servicio.entity.Servicio;
import com.nexoservice.servicio.mapper.ServicioMapper;
import com.nexoservice.servicio.repository.ServicioRepository;
import com.nexoservice.servicio.service.ServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ServicioServiceImpl implements ServicioService {

    private final ServicioRepository servicioRepository;
    private final ServicioMapper servicioMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ServicioResponse> listar(String buscar, Pageable pageable) {
        Page<Servicio> servicios;

        if (StringUtils.hasText(buscar)) {
            String criterio = buscar.trim();
            servicios = servicioRepository.findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(
                    criterio,
                    criterio,
                    pageable
            );
        } else {
            servicios = servicioRepository.findAll(pageable);
        }

        return servicios.map(servicioMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ServicioResponse> listarActivos(Pageable pageable) {
        return servicioRepository.findByActivoTrue(pageable).map(servicioMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ServicioResponse buscarPorId(Long id) {
        return servicioMapper.toResponse(obtenerServicio(id));
    }

    @Override
    @Transactional
    public ServicioResponse crear(ServicioCreateRequest request) {
        validarNombreDuplicado(request.nombre());

        Servicio servicio = servicioMapper.toEntity(request);
        Servicio servicioGuardado = servicioRepository.save(servicio);

        return servicioMapper.toResponse(servicioGuardado);
    }

    @Override
    @Transactional
    public ServicioResponse actualizar(Long id, ServicioUpdateRequest request) {
        Servicio servicio = obtenerServicio(id);
        validarNombreDuplicadoEnOtroServicio(request.nombre(), id);

        servicioMapper.updateEntity(servicio, request);

        return servicioMapper.toResponse(servicio);
    }

    @Override
    @Transactional
    public ServicioResponse cambiarEstado(Long id, ServicioEstadoRequest request) {
        Servicio servicio = obtenerServicio(id);
        servicio.setActivo(request.activo());

        return servicioMapper.toResponse(servicio);
    }

    private Servicio obtenerServicio(Long id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el servicio con id " + id));
    }

    private void validarNombreDuplicado(String nombre) {
        String nombreNormalizado = normalizarNombre(nombre);

        if (servicioRepository.existsByNombreIgnoreCase(nombreNormalizado)) {
            throw new DuplicateResourceException("Ya existe un servicio con el nombre " + nombreNormalizado);
        }
    }

    private void validarNombreDuplicadoEnOtroServicio(String nombre, Long id) {
        String nombreNormalizado = normalizarNombre(nombre);

        if (servicioRepository.existsByNombreIgnoreCaseAndIdNot(nombreNormalizado, id)) {
            throw new DuplicateResourceException("Ya existe otro servicio con el nombre " + nombreNormalizado);
        }
    }

    private String normalizarNombre(String nombre) {
        return nombre == null ? null : nombre.trim();
    }
}
