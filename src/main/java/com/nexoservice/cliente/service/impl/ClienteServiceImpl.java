package com.nexoservice.cliente.service.impl;

import com.nexoservice.cliente.dto.request.ClienteCreateRequest;
import com.nexoservice.cliente.dto.request.ClienteEstadoRequest;
import com.nexoservice.cliente.dto.request.ClienteUpdateRequest;
import com.nexoservice.cliente.dto.response.ClienteResponse;
import com.nexoservice.cliente.entity.Cliente;
import com.nexoservice.cliente.entity.TipoDocumento;
import com.nexoservice.cliente.mapper.ClienteMapper;
import com.nexoservice.cliente.repository.ClienteRepository;
import com.nexoservice.cliente.service.ClienteService;
import com.nexoservice.exception.DuplicateResourceException;
import com.nexoservice.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ClienteResponse> listar(String buscar, Pageable pageable) {
        Page<Cliente> clientes;

        if (StringUtils.hasText(buscar)) {
            String criterio = buscar.trim();
            clientes = clienteRepository.findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCaseOrNumeroDocumentoContainingIgnoreCase(
                    criterio,
                    criterio,
                    criterio,
                    pageable
            );
        } else {
            clientes = clienteRepository.findAll(pageable);
        }

        return clientes.map(clienteMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(Long id) {
        return clienteMapper.toResponse(obtenerCliente(id));
    }

    @Override
    @Transactional
    public ClienteResponse crear(ClienteCreateRequest request) {
        validarDocumentoDuplicado(request.tipoDocumento(), request.numeroDocumento());

        Cliente cliente = clienteMapper.toEntity(request);
        Cliente clienteGuardado = clienteRepository.save(cliente);

        return clienteMapper.toResponse(clienteGuardado);
    }

    @Override
    @Transactional
    public ClienteResponse actualizar(Long id, ClienteUpdateRequest request) {
        Cliente cliente = obtenerCliente(id);

        validarDocumentoDuplicadoEnOtroCliente(request.tipoDocumento(), request.numeroDocumento(), id);
        clienteMapper.updateEntity(cliente, request);

        return clienteMapper.toResponse(cliente);
    }

    @Override
    @Transactional
    public ClienteResponse cambiarEstado(Long id, ClienteEstadoRequest request) {
        Cliente cliente = obtenerCliente(id);
        cliente.setActivo(request.activo());

        return clienteMapper.toResponse(cliente);
    }

    private Cliente obtenerCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el cliente con id " + id));
    }

    private void validarDocumentoDuplicado(TipoDocumento tipoDocumento, String numeroDocumento) {
        if (clienteRepository.existsByTipoDocumentoAndNumeroDocumento(tipoDocumento, numeroDocumento)) {
            throw new DuplicateResourceException("Ya existe un cliente con el documento " + tipoDocumento + " " + numeroDocumento);
        }
    }

    private void validarDocumentoDuplicadoEnOtroCliente(
            TipoDocumento tipoDocumento,
            String numeroDocumento,
            Long id
    ) {
        if (clienteRepository.existsByTipoDocumentoAndNumeroDocumentoAndIdNot(tipoDocumento, numeroDocumento, id)) {
            throw new DuplicateResourceException("Ya existe otro cliente con el documento " + tipoDocumento + " " + numeroDocumento);
        }
    }
}
