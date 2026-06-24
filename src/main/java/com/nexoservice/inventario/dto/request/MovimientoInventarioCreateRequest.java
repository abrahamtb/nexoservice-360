package com.nexoservice.inventario.dto.request;

import com.nexoservice.inventario.entity.TipoMovimientoInventario;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MovimientoInventarioCreateRequest(
        @NotNull(message = "El producto es obligatorio")
        Long productoId,

        Long ordenServicioId,

        @NotNull(message = "El tipo de movimiento es obligatorio")
        TipoMovimientoInventario tipoMovimiento,

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser mayor a cero")
        Integer cantidad,

        @Size(max = 500, message = "El motivo no debe superar 500 caracteres")
        String motivo,

        @Size(max = 120, message = "El usuario responsable no debe superar 120 caracteres")
        String usuarioResponsable
) {
}
