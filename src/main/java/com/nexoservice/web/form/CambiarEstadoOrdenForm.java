package com.nexoservice.web.form;

import com.nexoservice.orden.entity.EstadoOrden;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CambiarEstadoOrdenForm {

    private EstadoOrden estadoNuevo;
    private String usuarioResponsable;
    private String observacion;
}
