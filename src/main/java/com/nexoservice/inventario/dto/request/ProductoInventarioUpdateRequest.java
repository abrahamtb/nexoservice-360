package com.nexoservice.inventario.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductoInventarioUpdateRequest(
        @NotBlank(message = "El codigo es obligatorio")
        @Size(max = 60, message = "El codigo no debe superar 60 caracteres")
        String codigo,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 160, message = "El nombre no debe superar 160 caracteres")
        String nombre,

        @Size(max = 300, message = "La descripcion no debe superar 300 caracteres")
        String descripcion,

        @Size(max = 80, message = "La categoria no debe superar 80 caracteres")
        String categoria,

        @Size(max = 30, message = "La unidad de medida no debe superar 30 caracteres")
        String unidadMedida,

        @NotNull(message = "El stock minimo es obligatorio")
        @Min(value = 0, message = "El stock minimo no puede ser negativo")
        Integer stockMinimo,

        @DecimalMin(value = "0.00", message = "El costo unitario no puede ser negativo")
        BigDecimal costoUnitario,

        @DecimalMin(value = "0.00", message = "El precio de referencia no puede ser negativo")
        BigDecimal precioReferencia
) {
}
