package com.nexoservice.inventario.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "productos_inventario",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_productos_inventario_codigo", columnNames = "codigo")
        }
)
public class ProductoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String codigo;

    @Column(nullable = false, length = 160)
    private String nombre;

    @Column(length = 300)
    private String descripcion;

    @Column(length = 80)
    private String categoria;

    @Column(length = 30)
    private String unidadMedida;

    @Column(nullable = false)
    private Integer stockActual;

    @Column(nullable = false)
    private Integer stockMinimo;

    @Column(name = "costo_unitario", precision = 10, scale = 2)
    private BigDecimal costoUnitario;

    @Column(name = "precio_referencia", precision = 10, scale = 2)
    private BigDecimal precioReferencia;

    @Column(nullable = false)
    private Boolean activo;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    void prePersist() {
        activo = activo == null || activo;
        stockActual = stockActual == null ? 0 : stockActual;
        stockMinimo = stockMinimo == null ? 0 : stockMinimo;
        unidadMedida = unidadMedida == null || unidadMedida.isBlank() ? "UND" : unidadMedida;
        fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}
