package com.nexoservice.cliente.dto.request;

import com.nexoservice.cliente.entity.TipoDocumento;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClienteCreateRequest(
        @NotBlank(message = "Los nombres son obligatorios")
        @Size(max = 120, message = "Los nombres no deben superar 120 caracteres")
        String nombres,

        @Size(max = 120, message = "Los apellidos no deben superar 120 caracteres")
        String apellidos,

        @NotNull(message = "El tipo de documento es obligatorio")
        TipoDocumento tipoDocumento,

        @NotBlank(message = "El numero de documento es obligatorio")
        @Pattern(regexp = "^[0-9A-Za-z-]{8,20}$", message = "El numero de documento debe tener entre 8 y 20 caracteres validos")
        String numeroDocumento,

        @Email(message = "El correo no tiene un formato valido")
        @Size(max = 120, message = "El correo no debe superar 120 caracteres")
        String correo,

        @Pattern(regexp = "^$|^[0-9+() -]{6,20}$", message = "El telefono no tiene un formato valido")
        String telefono,

        @Size(max = 180, message = "La direccion no debe superar 180 caracteres")
        String direccion
) {
}
