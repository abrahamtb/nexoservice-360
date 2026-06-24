package com.nexoservice.orden.entity;

import com.nexoservice.cliente.entity.Cliente;
import com.nexoservice.equipo.entity.Equipo;
import com.nexoservice.servicio.entity.Servicio;
import com.nexoservice.tecnico.entity.Tecnico;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ordenes_servicio")
public class OrdenServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "equipo_id", nullable = false)
    private Equipo equipo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "servicio_id", nullable = false)
    private Servicio servicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tecnico_id")
    private Tecnico tecnico;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoOrden estado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PrioridadOrden prioridad;

    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDateTime fechaIngreso;

    @Column(name = "fecha_estimada")
    private LocalDate fechaEstimada;

    @Column(name = "descripcion_problema", nullable = false, length = 500)
    private String descripcionProblema;

    @Column(length = 500)
    private String observaciones;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    void prePersist() {
        estado = estado == null ? EstadoOrden.RECIBIDA : estado;
        prioridad = prioridad == null ? PrioridadOrden.MEDIA : prioridad;
        fechaIngreso = fechaIngreso == null ? LocalDateTime.now() : fechaIngreso;
        fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}
