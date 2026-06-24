package com.nexoservice.web.form;

import com.nexoservice.pago.entity.MetodoPago;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PagoForm {

    private Long ordenServicioId;
    private Long cotizacionId;
    private BigDecimal monto;
    private MetodoPago metodoPago = MetodoPago.EFECTIVO;
    private String numeroOperacion;
    private String observaciones;
    private String usuarioResponsable;
}
