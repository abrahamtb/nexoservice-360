package com.nexoservice.reporte.dto;

import lombok.Value;

@Value
public class ClienteReporteRow {
    Long id;
    String nombre;
    String documento;
    String correo;
    String telefono;
    String direccion;
    String estado;
    String fechaRegistro;
}
