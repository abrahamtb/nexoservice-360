package com.nexoservice.tecnico.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TecnicoUpdateRequest(
        @NotBlank(message = "Los nombres son obligatorios")
        @Size(max = 120, message = "Los nombres no deben superar 120 caracteres")
        String nombres,

        @NotBlank(message = "Los apellidos son obligatorios")
        @Size(max = 120, message = "Los apellidos no deben superar 120 caracteres")
        String apellidos,

        @NotBlank(message = "La especialidad es obligatoria")
        @Size(max = 120, message = "La especialidad no debe superar 120 caracteres")
        String especialidad,

        @Email(message = "El correo no tiene un formato valido")
        @Size(max = 120, message = "El correo no debe superar 120 caracteres")
        String correo,

        @Pattern(regexp = "^$|^[0-9+() -]{6,20}$", message = "El telefono no tiene un formato valido")
        String telefono
) {
}
