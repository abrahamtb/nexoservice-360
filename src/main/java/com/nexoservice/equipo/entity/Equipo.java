package com.nexoservice.equipo.entity;

import com.nexoservice.cliente.entity.Cliente;
import com.nexoservice.tipoactivo.entity.TipoActivo;
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
        name = "equipos",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_equipos_numero_serie", columnNames = "numero_serie")
        }
)
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(nullable = false, length = 80)
    private String tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_activo_id")
    private TipoActivo tipoActivo;

    @Column(nullable = false, length = 80)
    private String marca;

    @Column(length = 100)
    private String modelo;

    @Column(name = "numero_serie", length = 100)
    private String numeroSerie;

    @Column(name = "codigo_interno", length = 80)
    private String codigoInterno;

    @Column(length = 150)
    private String ubicacion;

    @Column(length = 700)
    private String caracteristicas;

    @Column(length = 300)
    private String descripcion;

    @Column(length = 500)
    private String observaciones;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoEquipo estado;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    void prePersist() {
        estado = estado == null ? EstadoEquipo.ACTIVO : estado;
        fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}
