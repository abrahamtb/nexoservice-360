package com.nexoservice.reporte.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CotizacionDetalleReporteRow {

    private String descripcion;
    private Integer cantidad;
    private String precioUnitario;
    private String subtotal;
}
