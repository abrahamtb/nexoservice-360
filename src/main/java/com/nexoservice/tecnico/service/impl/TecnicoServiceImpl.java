package com.nexoservice.tecnico.service.impl;

import com.nexoservice.exception.BusinessException;
import com.nexoservice.exception.DuplicateResourceException;
import com.nexoservice.exception.ResourceNotFoundException;
import com.nexoservice.tecnico.dto.request.TecnicoCreateRequest;
import com.nexoservice.tecnico.dto.request.TecnicoDisponibilidadRequest;
import com.nexoservice.tecnico.dto.request.TecnicoEstadoRequest;
import com.nexoservice.tecnico.dto.request.TecnicoUpdateRequest;
import com.nexoservice.tecnico.dto.response.TecnicoResponse;
import com.nexoservice.tecnico.entity.DisponibilidadTecnico;
import com.nexoservice.tecnico.entity.Tecnico;
import com.nexoservice.tecnico.mapper.TecnicoMapper;
import com.nexoservice.tecnico.repository.TecnicoRepository;
import com.nexoservice.tecnico.service.TecnicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class TecnicoServiceImpl implements TecnicoService {

    private final TecnicoRepository tecnicoRepository;
    private final TecnicoMapper tecnicoMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<TecnicoResponse> listar(String buscar, Pageable pageable) {
        Page<Tecnico> tecnicos;

        if (StringUtils.hasText(buscar)) {
            String criterio = buscar.trim();
            tecnicos = tecnicoRepository.findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCaseOrEspecialidadContainingIgnoreCaseOrCorreoContainingIgnoreCase(
                    criterio,
                    criterio,
                    criterio,
                    criterio,
                    pageable
            );
        } else {
            tecnicos = tecnicoRepository.findAll(pageable);
        }

        return tecnicos.map(tecnicoMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TecnicoResponse> listarDisponibles(Pageable pageable) {
        return tecnicoRepository.findByActivoTrueAndDisponibilidad(DisponibilidadTecnico.DISPONIBLE, pageable)
                .map(tecnicoMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public TecnicoResponse buscarPorId(Long id) {
        return tecnicoMapper.toResponse(obtenerTecnico(id));
    }

    @Override
    @Transactional
    public TecnicoResponse crear(TecnicoCreateRequest request) {
        validarCorreoDuplicado(request.correo());

        Tecnico tecnico = tecnicoMapper.toEntity(request);
        Tecnico tecnicoGuardado = tecnicoRepository.save(tecnico);

        return tecnicoMapper.toResponse(tecnicoGuardado);
    }

    @Override
    @Transactional
    public TecnicoResponse actualizar(Long id, TecnicoUpdateRequest request) {
        Tecnico tecnico = obtenerTecnico(id);
        validarCorreoDuplicadoEnOtroTecnico(request.correo(), id);

        tecnicoMapper.updateEntity(tecnico, request);

        return tecnicoMapper.toResponse(tecnico);
    }

    @Override
    @Transactional
    public TecnicoResponse cambiarEstado(Long id, TecnicoEstadoRequest request) {
        Tecnico tecnico = obtenerTecnico(id);
        tecnico.setActivo(request.activo());

        if (!Boolean.TRUE.equals(request.activo())) {
            tecnico.setDisponibilidad(DisponibilidadTecnico.NO_DISPONIBLE);
        }

        return tecnicoMapper.toResponse(tecnico);
    }

    @Override
    @Transactional
    public TecnicoResponse cambiarDisponibilidad(Long id, TecnicoDisponibilidadRequest request) {
        Tecnico tecnico = obtenerTecnico(id);

        if (!Boolean.TRUE.equals(tecnico.getActivo())) {
            throw new BusinessException("No se puede cambiar la disponibilidad de un tecnico inactivo");
        }

        tecnico.setDisponibilidad(request.disponibilidad());

        return tecnicoMapper.toResponse(tecnico);
    }

    private Tecnico obtenerTecnico(Long id) {
        return tecnicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el tecnico con id " + id));
    }

    private void validarCorreoDuplicado(String correo) {
        String correoNormalizado = normalizarCorreo(correo);

        if (correoNormalizado != null && tecnicoRepository.existsByCorreo(correoNormalizado)) {
            throw new DuplicateResourceException("Ya existe un tecnico con el correo " + correoNormalizado);
        }
    }

    private void validarCorreoDuplicadoEnOtroTecnico(String correo, Long id) {
        String correoNormalizado = normalizarCorreo(correo);

        if (correoNormalizado != null && tecnicoRepository.existsByCorreoAndIdNot(correoNormalizado, id)) {
            throw new DuplicateResourceException("Ya existe otro tecnico con el correo " + correoNormalizado);
        }
    }

    private String normalizarCorreo(String correo) {
        return correo == null || correo.isBlank() ? null : correo.trim().toLowerCase();
    }
}
