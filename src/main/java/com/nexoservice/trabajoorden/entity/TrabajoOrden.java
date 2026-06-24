package com.nexoservice.trabajoorden.entity;

import com.nexoservice.orden.entity.OrdenServicio;
import com.nexoservice.tecnico.entity.Tecnico;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trabajos_orden")
public class TrabajoOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orden_servicio_id", nullable = false)
    private OrdenServicio ordenServicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tecnico_id")
    private Tecnico tecnico;

    @Column(nullable = false, length = 160)
    private String actividad;

    @Column(nullable = false, length = 1000)
    private String detalle;

    @Column(name = "fecha_trabajo", nullable = false)
    private LocalDateTime fechaTrabajo;

    @Column(name = "tiempo_invertido_minutos")
    private Integer tiempoInvertidoMinutos;

    @Column(length = 500)
    private String observaciones;

    @Column(name = "usuario_responsable", length = 120)
    private String usuarioResponsable;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    void prePersist() {
        fechaTrabajo = fechaTrabajo == null ? LocalDateTime.now() : fechaTrabajo;
        fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}
