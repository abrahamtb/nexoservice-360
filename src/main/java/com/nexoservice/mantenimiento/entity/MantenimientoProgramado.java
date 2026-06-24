package com.nexoservice.mantenimiento.entity;

import com.nexoservice.equipo.entity.Equipo;
import com.nexoservice.servicio.entity.Servicio;
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
@Table(name = "mantenimientos_programados")
public class MantenimientoProgramado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "equipo_id", nullable = false)
    private Equipo equipo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "servicio_id", nullable = false)
    private Servicio servicio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private FrecuenciaMantenimiento frecuencia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoMantenimientoProgramado estado;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "proxima_fecha", nullable = false)
    private LocalDate proximaFecha;

    @Column(name = "ultima_orden_id")
    private Long ultimaOrdenId;

    @Column(length = 500)
    private String observaciones;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    void prePersist() {
        estado = estado == null ? EstadoMantenimientoProgramado.ACTIVO : estado;
        fechaInicio = fechaInicio == null ? LocalDate.now() : fechaInicio;
        proximaFecha = proximaFecha == null ? fechaInicio : proximaFecha;
        fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}
