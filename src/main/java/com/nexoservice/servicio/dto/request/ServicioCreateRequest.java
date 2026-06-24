package com.nexoservice.servicio.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ServicioCreateRequest(
        @NotBlank(message = "El nombre del servicio es obligatorio")
        @Size(max = 120, message = "El nombre no debe superar 120 caracteres")
        String nombre,

        @Size(max = 300, message = "La descripcion no debe superar 300 caracteres")
        String descripcion,

        @DecimalMin(value = "0.00", message = "El precio base no puede ser negativo")
        BigDecimal precioBase,

        @Min(value = 1, message = "La duracion estimada debe ser mayor a cero")
        Integer duracionEstimadaMinutos
) {
}
