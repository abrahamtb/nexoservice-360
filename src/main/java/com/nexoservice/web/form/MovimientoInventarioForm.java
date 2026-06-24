package com.nexoservice.web.form;

import com.nexoservice.inventario.entity.TipoMovimientoInventario;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovimientoInventarioForm {

    private Long productoId;
    private Long ordenServicioId;
    private TipoMovimientoInventario tipoMovimiento = TipoMovimientoInventario.ENTRADA;
    private Integer cantidad = 1;
    private String motivo;
    private String usuarioResponsable;
}
