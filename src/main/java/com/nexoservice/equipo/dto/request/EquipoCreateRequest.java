package com.nexoservice.equipo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EquipoCreateRequest(
        @NotNull(message = "El cliente es obligatorio")
        Long clienteId,

        Long tipoActivoId,

        @Size(max = 80, message = "El tipo no debe superar 80 caracteres")
        String tipo,

        @NotBlank(message = "La marca es obligatoria")
        @Size(max = 80, message = "La marca no debe superar 80 caracteres")
        String marca,

        @Size(max = 100, message = "El modelo no debe superar 100 caracteres")
        String modelo,

        @Size(max = 100, message = "El numero de serie no debe superar 100 caracteres")
        String numeroSerie,

        @Size(max = 80, message = "El codigo interno no debe superar 80 caracteres")
        String codigoInterno,

        @Size(max = 150, message = "La ubicacion no debe superar 150 caracteres")
        String ubicacion,

        @Size(max = 700, message = "Las caracteristicas no deben superar 700 caracteres")
        String caracteristicas,

        @Size(max = 300, message = "La descripcion no debe superar 300 caracteres")
        String descripcion,

        @Size(max = 500, message = "Las observaciones no deben superar 500 caracteres")
        String observaciones
) {
}
