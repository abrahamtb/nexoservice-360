package com.nexoservice.inventario.service;

import com.nexoservice.inventario.dto.request.MovimientoInventarioCreateRequest;
import com.nexoservice.inventario.dto.request.ProductoInventarioCreateRequest;
import com.nexoservice.inventario.dto.request.ProductoInventarioEstadoRequest;
import com.nexoservice.inventario.dto.request.ProductoInventarioUpdateRequest;
import com.nexoservice.inventario.dto.response.MovimientoInventarioResponse;
import com.nexoservice.inventario.dto.response.ProductoInventarioResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InventarioService {

    Page<ProductoInventarioResponse> listarProductos(String buscar, Pageable pageable);

    Page<ProductoInventarioResponse> listarProductosActivos(Pageable pageable);

    ProductoInventarioResponse buscarProductoPorId(Long id);

    ProductoInventarioResponse crearProducto(ProductoInventarioCreateRequest request);

    ProductoInventarioResponse actualizarProducto(Long id, ProductoInventarioUpdateRequest request);

    ProductoInventarioResponse cambiarEstadoProducto(Long id, ProductoInventarioEstadoRequest request);

    MovimientoInventarioResponse registrarMovimiento(MovimientoInventarioCreateRequest request);

    Page<MovimientoInventarioResponse> listarMovimientos(Long productoId, Long ordenServicioId, Pageable pageable);
}
