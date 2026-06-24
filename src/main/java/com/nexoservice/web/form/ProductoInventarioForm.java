package com.nexoservice.web.form;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductoInventarioForm {

    private String codigo;
    private String nombre;
    private String descripcion;
    private String categoria;
    private String unidadMedida;
    private Integer stockInicial = 0;
    private Integer stockMinimo = 0;
    private BigDecimal costoUnitario;
    private BigDecimal precioReferencia;
}
