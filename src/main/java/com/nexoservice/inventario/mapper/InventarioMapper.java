package com.nexoservice.inventario.mapper;

import com.nexoservice.inventario.dto.request.ProductoInventarioCreateRequest;
import com.nexoservice.inventario.dto.request.ProductoInventarioUpdateRequest;
import com.nexoservice.inventario.dto.response.MovimientoInventarioResponse;
import com.nexoservice.inventario.dto.response.ProductoInventarioResponse;
import com.nexoservice.inventario.entity.MovimientoInventario;
import com.nexoservice.inventario.entity.ProductoInventario;
import org.springframework.stereotype.Component;

@Component
public class InventarioMapper {

    public ProductoInventario toEntity(ProductoInventarioCreateRequest request) {
        return ProductoInventario.builder()
                .codigo(normalizar(request.codigo()))
                .nombre(request.nombre())
                .descripcion(request.descripcion())
                .categoria(request.categoria())
                .unidadMedida(normalizar(request.unidadMedida()))
                .stockActual(0)
                .stockMinimo(request.stockMinimo())
                .costoUnitario(request.costoUnitario())
                .precioReferencia(request.precioReferencia())
                .build();
    }

    public void updateEntity(ProductoInventario producto, ProductoInventarioUpdateRequest request) {
        producto.setCodigo(normalizar(request.codigo()));
        producto.setNombre(request.nombre());
        producto.setDescripcion(request.descripcion());
        producto.setCategoria(request.categoria());
        producto.setUnidadMedida(normalizar(request.unidadMedida()));
        producto.setStockMinimo(request.stockMinimo());
        producto.setCostoUnitario(request.costoUnitario());
        producto.setPrecioReferencia(request.precioReferencia());
    }

    public ProductoInventarioResponse toResponse(ProductoInventario producto) {
        return new ProductoInventarioResponse(
                producto.getId(),
                producto.getCodigo(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getCategoria(),
                producto.getUnidadMedida(),
                producto.getStockActual(),
                producto.getStockMinimo(),
                producto.getStockActual() <= producto.getStockMinimo(),
                producto.getCostoUnitario(),
                producto.getPrecioReferencia(),
                producto.getActivo(),
                producto.getFechaCreacion(),
                producto.getFechaActualizacion()
        );
    }

    public MovimientoInventarioResponse toResponse(MovimientoInventario movimiento) {
        ProductoInventario producto = movimiento.getProducto();

        return new MovimientoInventarioResponse(
                movimiento.getId(),
                producto.getId(),
                producto.getCodigo(),
                producto.getNombre(),
                movimiento.getOrdenServicio() == null ? null : movimiento.getOrdenServicio().getId(),
                movimiento.getTipoMovimiento(),
                movimiento.getCantidad(),
                movimiento.getStockResultante(),
                movimiento.getMotivo(),
                movimiento.getUsuarioResponsable(),
                movimiento.getFechaMovimiento()
        );
    }

    private String normalizar(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }
}
