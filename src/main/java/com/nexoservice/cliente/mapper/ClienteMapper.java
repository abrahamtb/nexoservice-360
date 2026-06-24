package com.nexoservice.cliente.mapper;

import com.nexoservice.cliente.dto.request.ClienteCreateRequest;
import com.nexoservice.cliente.dto.request.ClienteUpdateRequest;
import com.nexoservice.cliente.dto.response.ClienteResponse;
import com.nexoservice.cliente.entity.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public Cliente toEntity(ClienteCreateRequest request) {
        return Cliente.builder()
                .nombres(request.nombres())
                .apellidos(request.apellidos())
                .tipoDocumento(request.tipoDocumento())
                .numeroDocumento(request.numeroDocumento())
                .correo(request.correo())
                .telefono(request.telefono())
                .direccion(request.direccion())
                .activo(true)
                .build();
    }

    public void updateEntity(Cliente cliente, ClienteUpdateRequest request) {
        cliente.setNombres(request.nombres());
        cliente.setApellidos(request.apellidos());
        cliente.setTipoDocumento(request.tipoDocumento());
        cliente.setNumeroDocumento(request.numeroDocumento());
        cliente.setCorreo(request.correo());
        cliente.setTelefono(request.telefono());
        cliente.setDireccion(request.direccion());
    }

    public ClienteResponse toResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNombres(),
                cliente.getApellidos(),
                cliente.getTipoDocumento(),
                cliente.getNumeroDocumento(),
                cliente.getCorreo(),
                cliente.getTelefono(),
                cliente.getDireccion(),
                cliente.getActivo(),
                cliente.getFechaCreacion(),
                cliente.getFechaActualizacion()
        );
    }
}
