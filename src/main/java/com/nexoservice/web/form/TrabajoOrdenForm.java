package com.nexoservice.web.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrabajoOrdenForm {

    @NotNull(message = "La orden de servicio es obligatoria")
    private Long ordenServicioId;

    private Long tecnicoId;

    @NotBlank(message = "La actividad es obligatoria")
    @Size(max = 160, message = "La actividad no debe superar 160 caracteres")
    private String actividad;

    @NotBlank(message = "El detalle del trabajo es obligatorio")
    @Size(max = 1000, message = "El detalle no debe superar 1000 caracteres")
    private String detalle;

    @Min(value = 1, message = "El tiempo invertido debe ser mayor a cero")
    private Integer tiempoInvertidoMinutos;

    @Size(max = 500, message = "Las observaciones no deben superar 500 caracteres")
    private String observaciones;

    @Size(max = 120, message = "El usuario responsable no debe superar 120 caracteres")
    private String usuarioResponsable;
}
