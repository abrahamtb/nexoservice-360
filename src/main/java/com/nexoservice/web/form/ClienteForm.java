package com.nexoservice.web.form;

import com.nexoservice.cliente.entity.TipoDocumento;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteForm {

    private String nombres;
    private String apellidos;
    private TipoDocumento tipoDocumento = TipoDocumento.DNI;
    private String numeroDocumento;
    private String correo;
    private String telefono;
    private String direccion;
}
