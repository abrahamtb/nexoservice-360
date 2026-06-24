package com.nexoservice.web.form;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ServicioForm {

    private String nombre;
    private String descripcion;
    private BigDecimal precioBase;
    private Integer duracionEstimadaMinutos;
}
