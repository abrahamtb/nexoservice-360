package com.nexoservice.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TipoActivoForm {

    @NotBlank(message = "El nombre del tipo de activo es obligatorio")
    @Size(max = 120, message = "El nombre no debe superar 120 caracteres")
    private String nombre;

    @Size(max = 300, message = "La descripcion no debe superar 300 caracteres")
    private String descripcion;
}
