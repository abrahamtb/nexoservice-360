package com.nexoservice.reporte.dto;

import lombok.Value;

@Value
public class OrdenReporteRow {
    Long id;
    String cliente;
    String equipo;
    String servicio;
    String tecnico;
    String estado;
    String prioridad;
    String fechaIngreso;
    String fechaEstimada;
}
