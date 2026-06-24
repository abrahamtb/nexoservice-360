package com.nexoservice.cotizacion.entity;

import com.nexoservice.orden.entity.OrdenServicio;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "cotizaciones",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_cotizaciones_numero", columnNames = "numero")
        }
)
public class Cotizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30)
    private String numero;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orden_servicio_id", nullable = false)
    private OrdenServicio ordenServicio;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoCotizacion estado;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotal;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal impuesto;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal total;

    @Column(length = 500)
    private String observaciones;

    @OneToMany(mappedBy = "cotizacion", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CotizacionDetalle> detalles = new ArrayList<>();

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    public void agregarDetalle(CotizacionDetalle detalle) {
        detalles.add(detalle);
        detalle.setCotizacion(this);
    }

    public void limpiarDetalles() {
        detalles.clear();
    }

    @PrePersist
    void prePersist() {
        fechaEmision = fechaEmision == null ? LocalDate.now() : fechaEmision;
        estado = estado == null ? EstadoCotizacion.PENDIENTE : estado;
        subtotal = subtotal == null ? BigDecimal.ZERO : subtotal;
        impuesto = impuesto == null ? BigDecimal.ZERO : impuesto;
        total = total == null ? BigDecimal.ZERO : total;
        fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}
