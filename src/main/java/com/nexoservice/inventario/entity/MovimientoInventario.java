package com.nexoservice.inventario.entity;

import com.nexoservice.orden.entity.OrdenServicio;
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
@Table(name = "movimientos_inventario")
public class MovimientoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private ProductoInventario producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_servicio_id")
    private OrdenServicio ordenServicio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoMovimientoInventario tipoMovimiento;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "stock_resultante", nullable = false)
    private Integer stockResultante;

    @Column(length = 500)
    private String motivo;

    @Column(name = "usuario_responsable", length = 120)
    private String usuarioResponsable;

    @Column(name = "fecha_movimiento", nullable = false)
    private LocalDateTime fechaMovimiento;

    @PrePersist
    void prePersist() {
        fechaMovimiento = fechaMovimiento == null ? LocalDateTime.now() : fechaMovimiento;
    }
}
