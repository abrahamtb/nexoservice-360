package com.nexoservice.diagnostico.entity;

import com.nexoservice.orden.entity.OrdenServicio;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(
        name = "diagnosticos",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_diagnosticos_orden", columnNames = "orden_servicio_id")
        }
)
public class Diagnostico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orden_servicio_id", nullable = false)
    private OrdenServicio ordenServicio;

    @Column(name = "descripcion_falla", nullable = false, length = 700)
    private String descripcionFalla;

    @Column(name = "causa_probable", length = 500)
    private String causaProbable;

    @Column(name = "solucion_recomendada", nullable = false, length = 700)
    private String solucionRecomendada;

    @Column(length = 500)
    private String observaciones;

    @Column(name = "fecha_diagnostico", nullable = false)
    private LocalDateTime fechaDiagnostico;

    @Column(name = "usuario_responsable", length = 120)
    private String usuarioResponsable;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    void prePersist() {
        fechaDiagnostico = fechaDiagnostico == null ? LocalDateTime.now() : fechaDiagnostico;
        fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}
