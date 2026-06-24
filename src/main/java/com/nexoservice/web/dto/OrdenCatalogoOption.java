package com.nexoservice.web.dto;

public record OrdenCatalogoOption(
        Long id,
        String clienteNombre,
        String equipoResumen,
        String estado
) {

    public String etiqueta() {
        return "#" + id + " - " + clienteNombre + " - " + estado;
    }
}
