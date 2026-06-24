package com.nexoservice.web.form;

import com.nexoservice.orden.entity.PrioridadOrden;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OrdenServicioForm {

    private Long clienteId;
    private Long equipoId;
    private Long servicioId;
    private Long tecnicoId;
    private PrioridadOrden prioridad = PrioridadOrden.MEDIA;
    private LocalDate fechaEstimada;
    private String descripcionProblema;
    private String observaciones;
    private String usuarioResponsable = "Recepcion";
}
