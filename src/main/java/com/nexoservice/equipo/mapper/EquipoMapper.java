package com.nexoservice.equipo.mapper;

import com.nexoservice.cliente.entity.Cliente;
import com.nexoservice.equipo.dto.request.EquipoCreateRequest;
import com.nexoservice.equipo.dto.request.EquipoUpdateRequest;
import com.nexoservice.equipo.dto.response.EquipoResponse;
import com.nexoservice.equipo.entity.Equipo;
import com.nexoservice.tipoactivo.entity.TipoActivo;
import org.springframework.stereotype.Component;

@Component
public class EquipoMapper {

    public Equipo toEntity(EquipoCreateRequest request, Cliente cliente, TipoActivo tipoActivo) {
        return Equipo.builder()
                .cliente(cliente)
                .tipoActivo(tipoActivo)
                .tipo(resolverTipo(request.tipo(), tipoActivo))
                .marca(request.marca())
                .modelo(request.modelo())
                .numeroSerie(normalizarNumeroSerie(request.numeroSerie()))
                .codigoInterno(normalizarTexto(request.codigoInterno()))
                .ubicacion(normalizarTexto(request.ubicacion()))
                .caracteristicas(request.caracteristicas())
                .descripcion(request.descripcion())
                .observaciones(request.observaciones())
                .build();
    }

    public void updateEntity(Equipo equipo, EquipoUpdateRequest request, TipoActivo tipoActivo) {
        equipo.setTipoActivo(tipoActivo);
        equipo.setTipo(resolverTipo(request.tipo(), tipoActivo));
        equipo.setMarca(request.marca());
        equipo.setModelo(request.modelo());
        equipo.setNumeroSerie(normalizarNumeroSerie(request.numeroSerie()));
        equipo.setCodigoInterno(normalizarTexto(request.codigoInterno()));
        equipo.setUbicacion(normalizarTexto(request.ubicacion()));
        equipo.setCaracteristicas(request.caracteristicas());
        equipo.setDescripcion(request.descripcion());
        equipo.setObservaciones(request.observaciones());
    }

    public EquipoResponse toResponse(Equipo equipo) {
        Cliente cliente = equipo.getCliente();
        TipoActivo tipoActivo = equipo.getTipoActivo();
        String clienteNombre = cliente.getApellidos() == null || cliente.getApellidos().isBlank()
                ? cliente.getNombres()
                : cliente.getNombres() + " " + cliente.getApellidos();

        return new EquipoResponse(
                equipo.getId(),
                cliente.getId(),
                clienteNombre,
                tipoActivo == null ? null : tipoActivo.getId(),
                tipoActivo == null ? null : tipoActivo.getNombre(),
                equipo.getTipo(),
                equipo.getMarca(),
                equipo.getModelo(),
                equipo.getNumeroSerie(),
                equipo.getCodigoInterno(),
                equipo.getUbicacion(),
                equipo.getCaracteristicas(),
                equipo.getDescripcion(),
                equipo.getObservaciones(),
                equipo.getEstado(),
                equipo.getFechaCreacion(),
                equipo.getFechaActualizacion()
        );
    }

    private String normalizarNumeroSerie(String numeroSerie) {
        return normalizarTexto(numeroSerie);
    }

    private String normalizarTexto(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }

    private String resolverTipo(String tipoTexto, TipoActivo tipoActivo) {
        if (tipoActivo != null) {
            return tipoActivo.getNombre();
        }

        return normalizarTexto(tipoTexto);
    }
}
