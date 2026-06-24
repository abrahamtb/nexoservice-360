package com.nexoservice.pago.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PagoAnularRequest(
        @NotBlank(message = "El motivo de anulacion es obligatorio")
        @Size(max = 500, message = "El motivo no debe superar 500 caracteres")
        String motivo,

        @Size(max = 120, message = "El usuario responsable no debe superar 120 caracteres")
        String usuarioResponsable
) {
}
