package com.nexoservice.equipo.service.impl;

import com.nexoservice.cliente.entity.Cliente;
import com.nexoservice.cliente.repository.ClienteRepository;
import com.nexoservice.equipo.dto.request.EquipoCreateRequest;
import com.nexoservice.equipo.dto.request.EquipoEstadoRequest;
import com.nexoservice.equipo.dto.request.EquipoUpdateRequest;
import com.nexoservice.equipo.dto.response.EquipoResponse;
import com.nexoservice.equipo.entity.Equipo;
import com.nexoservice.equipo.mapper.EquipoMapper;
import com.nexoservice.equipo.repository.EquipoRepository;
import com.nexoservice.equipo.service.EquipoService;
import com.nexoservice.exception.BusinessException;
import com.nexoservice.exception.DuplicateResourceException;
import com.nexoservice.exception.ResourceNotFoundException;
import com.nexoservice.tipoactivo.entity.TipoActivo;
import com.nexoservice.tipoactivo.repository.TipoActivoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class EquipoServiceImpl implements EquipoService {

    private final EquipoRepository equipoRepository;
    private final ClienteRepository clienteRepository;
    private final TipoActivoRepository tipoActivoRepository;
    private final EquipoMapper equipoMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<EquipoResponse> listar(String buscar, Pageable pageable) {
        Page<Equipo> equipos;

        if (StringUtils.hasText(buscar)) {
            String criterio = buscar.trim();
            equipos = equipoRepository.buscar(criterio, pageable);
        } else {
            equipos = equipoRepository.findAll(pageable);
        }

        return equipos.map(equipoMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EquipoResponse> listarPorCliente(Long clienteId, Pageable pageable) {
        validarClienteExiste(clienteId);
        return equipoRepository.findByClienteId(clienteId, pageable).map(equipoMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public EquipoResponse buscarPorId(Long id) {
        return equipoMapper.toResponse(obtenerEquipo(id));
    }

    @Override
    @Transactional
    public EquipoResponse crear(EquipoCreateRequest request) {
        Cliente cliente = obtenerCliente(request.clienteId());
        TipoActivo tipoActivo = obtenerTipoActivoOpcional(request.tipoActivoId());
        validarClienteActivo(cliente);
        validarTipoActivoActivo(tipoActivo);
        validarTipoInformado(tipoActivo, request.tipo());
        validarNumeroSerieDuplicado(request.numeroSerie());

        Equipo equipo = equipoMapper.toEntity(request, cliente, tipoActivo);
        Equipo equipoGuardado = equipoRepository.save(equipo);

        return equipoMapper.toResponse(equipoGuardado);
    }

    @Override
    @Transactional
    public EquipoResponse actualizar(Long id, EquipoUpdateRequest request) {
        Equipo equipo = obtenerEquipo(id);
        TipoActivo tipoActivo = obtenerTipoActivoOpcional(request.tipoActivoId());
        validarTipoActivoActivo(tipoActivo);
        validarTipoInformado(tipoActivo, request.tipo());
        validarNumeroSerieDuplicadoEnOtroEquipo(request.numeroSerie(), id);

        equipoMapper.updateEntity(equipo, request, tipoActivo);

        return equipoMapper.toResponse(equipo);
    }

    @Override
    @Transactional
    public EquipoResponse cambiarEstado(Long id, EquipoEstadoRequest request) {
        Equipo equipo = obtenerEquipo(id);
        equipo.setEstado(request.estado());

        return equipoMapper.toResponse(equipo);
    }

    private Equipo obtenerEquipo(Long id) {
        return equipoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el equipo con id " + id));
    }

    private Cliente obtenerCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el cliente con id " + id));
    }

    private void validarClienteExiste(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new ResourceNotFoundException("No se encontro el cliente con id " + clienteId);
        }
    }

    private void validarClienteActivo(Cliente cliente) {
        if (!Boolean.TRUE.equals(cliente.getActivo())) {
            throw new BusinessException("No se puede registrar un equipo para un cliente inactivo");
        }
    }

    private TipoActivo obtenerTipoActivoOpcional(Long id) {
        if (id == null) {
            return null;
        }

        return tipoActivoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el tipo de activo con id " + id));
    }

    private void validarTipoActivoActivo(TipoActivo tipoActivo) {
        if (tipoActivo != null && !Boolean.TRUE.equals(tipoActivo.getActivo())) {
            throw new BusinessException("No se puede asignar un tipo de activo inactivo");
        }
    }

    private void validarTipoInformado(TipoActivo tipoActivo, String tipoTexto) {
        if (tipoActivo == null && !StringUtils.hasText(tipoTexto)) {
            throw new BusinessException("Debe seleccionar un tipo de activo o escribir un tipo personalizado");
        }
    }

    private void validarNumeroSerieDuplicado(String numeroSerie) {
        if (StringUtils.hasText(numeroSerie) && equipoRepository.existsByNumeroSerie(numeroSerie.trim())) {
            throw new DuplicateResourceException("Ya existe un equipo con el numero de serie " + numeroSerie);
        }
    }

    private void validarNumeroSerieDuplicadoEnOtroEquipo(String numeroSerie, Long id) {
        if (StringUtils.hasText(numeroSerie) && equipoRepository.existsByNumeroSerieAndIdNot(numeroSerie.trim(), id)) {
            throw new DuplicateResourceException("Ya existe otro equipo con el numero de serie " + numeroSerie);
        }
    }
}
