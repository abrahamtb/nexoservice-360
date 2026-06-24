package com.nexoservice.web.form;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class CotizacionForm {

    private Long ordenServicioId;
    private LocalDate fechaVencimiento;
    private String observaciones;
    private String descripcion1;
    private Integer cantidad1 = 1;
    private BigDecimal precioUnitario1;
    private String descripcion2;
    private Integer cantidad2;
    private BigDecimal precioUnitario2;
    private String descripcion3;
    private Integer cantidad3;
    private BigDecimal precioUnitario3;
}
