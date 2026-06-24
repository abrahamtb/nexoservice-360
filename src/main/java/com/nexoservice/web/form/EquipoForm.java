package com.nexoservice.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipoForm {

    @NotNull(message = "El cliente es obligatorio")
    private Long clienteId;

    private Long tipoActivoId;

    @Size(max = 80, message = "El tipo no debe superar 80 caracteres")
    private String tipo;

    @NotBlank(message = "La marca es obligatoria")
    @Size(max = 80, message = "La marca no debe superar 80 caracteres")
    private String marca;

    @Size(max = 100, message = "El modelo no debe superar 100 caracteres")
    private String modelo;

    @Size(max = 100, message = "El numero de serie no debe superar 100 caracteres")
    private String numeroSerie;

    @Size(max = 80, message = "El codigo interno no debe superar 80 caracteres")
    private String codigoInterno;

    @Size(max = 150, message = "La ubicacion no debe superar 150 caracteres")
    private String ubicacion;

    @Size(max = 700, message = "Las caracteristicas no deben superar 700 caracteres")
    private String caracteristicas;

    @Size(max = 300, message = "La descripcion no debe superar 300 caracteres")
    private String descripcion;

    @Size(max = 500, message = "Las observaciones no deben superar 500 caracteres")
    private String observaciones;
}
