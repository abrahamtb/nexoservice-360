package com.nexoservice.tipoactivo.service.impl;

import com.nexoservice.exception.DuplicateResourceException;
import com.nexoservice.exception.ResourceNotFoundException;
import com.nexoservice.tipoactivo.dto.request.TipoActivoCreateRequest;
import com.nexoservice.tipoactivo.dto.request.TipoActivoEstadoRequest;
import com.nexoservice.tipoactivo.dto.request.TipoActivoUpdateRequest;
import com.nexoservice.tipoactivo.dto.response.TipoActivoResponse;
import com.nexoservice.tipoactivo.entity.TipoActivo;
import com.nexoservice.tipoactivo.mapper.TipoActivoMapper;
import com.nexoservice.tipoactivo.repository.TipoActivoRepository;
import com.nexoservice.tipoactivo.service.TipoActivoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class TipoActivoServiceImpl implements TipoActivoService {

    private final TipoActivoRepository tipoActivoRepository;
    private final TipoActivoMapper tipoActivoMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<TipoActivoResponse> listar(String buscar, Pageable pageable) {
        Page<TipoActivo> tiposActivo;

        if (StringUtils.hasText(buscar)) {
            String criterio = buscar.trim();
            tiposActivo = tipoActivoRepository.findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(
                    criterio,
                    criterio,
                    pageable
            );
        } else {
            tiposActivo = tipoActivoRepository.findAll(pageable);
        }

        return tiposActivo.map(tipoActivoMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TipoActivoResponse> listarActivos(Pageable pageable) {
        return tipoActivoRepository.findByActivoTrue(pageable).map(tipoActivoMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public TipoActivoResponse buscarPorId(Long id) {
        return tipoActivoMapper.toResponse(obtenerTipoActivo(id));
    }

    @Override
    @Transactional
    public TipoActivoResponse crear(TipoActivoCreateRequest request) {
        validarNombreDuplicado(request.nombre());

        TipoActivo tipoActivo = tipoActivoMapper.toEntity(request);
        TipoActivo tipoActivoGuardado = tipoActivoRepository.save(tipoActivo);

        return tipoActivoMapper.toResponse(tipoActivoGuardado);
    }

    @Override
    @Transactional
    public TipoActivoResponse actualizar(Long id, TipoActivoUpdateRequest request) {
        TipoActivo tipoActivo = obtenerTipoActivo(id);
        validarNombreDuplicadoEnOtroTipoActivo(request.nombre(), id);

        tipoActivoMapper.updateEntity(tipoActivo, request);

        return tipoActivoMapper.toResponse(tipoActivo);
    }

    @Override
    @Transactional
    public TipoActivoResponse cambiarEstado(Long id, TipoActivoEstadoRequest request) {
        TipoActivo tipoActivo = obtenerTipoActivo(id);
        tipoActivo.setActivo(request.activo());

        return tipoActivoMapper.toResponse(tipoActivo);
    }

    private TipoActivo obtenerTipoActivo(Long id) {
        return tipoActivoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el tipo de activo con id " + id));
    }

    private void validarNombreDuplicado(String nombre) {
        String nombreNormalizado = normalizarNombre(nombre);

        if (tipoActivoRepository.existsByNombreIgnoreCase(nombreNormalizado)) {
            throw new DuplicateResourceException("Ya existe un tipo de activo con el nombre " + nombreNormalizado);
        }
    }

    private void validarNombreDuplicadoEnOtroTipoActivo(String nombre, Long id) {
        String nombreNormalizado = normalizarNombre(nombre);

        if (tipoActivoRepository.existsByNombreIgnoreCaseAndIdNot(nombreNormalizado, id)) {
            throw new DuplicateResourceException("Ya existe otro tipo de activo con el nombre " + nombreNormalizado);
        }
    }

    private String normalizarNombre(String nombre) {
        return nombre == null ? null : nombre.trim();
    }
}
