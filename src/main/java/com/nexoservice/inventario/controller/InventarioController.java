package com.nexoservice.inventario.controller;

import com.nexoservice.inventario.dto.request.MovimientoInventarioCreateRequest;
import com.nexoservice.inventario.dto.request.ProductoInventarioCreateRequest;
import com.nexoservice.inventario.dto.request.ProductoInventarioEstadoRequest;
import com.nexoservice.inventario.dto.request.ProductoInventarioUpdateRequest;
import com.nexoservice.inventario.dto.response.MovimientoInventarioResponse;
import com.nexoservice.inventario.dto.response.ProductoInventarioResponse;
import com.nexoservice.inventario.service.InventarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;

    @GetMapping("/productos")
    public ResponseEntity<Page<ProductoInventarioResponse>> listarProductos(
            @RequestParam(required = false) String buscar,
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(inventarioService.listarProductos(buscar, pageable));
    }

    @GetMapping("/productos/activos")
    public ResponseEntity<Page<ProductoInventarioResponse>> listarProductosActivos(
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(inventarioService.listarProductosActivos(pageable));
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<ProductoInventarioResponse> buscarProductoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.buscarProductoPorId(id));
    }

    @PostMapping("/productos")
    public ResponseEntity<ProductoInventarioResponse> crearProducto(@Valid @RequestBody ProductoInventarioCreateRequest request) {
        ProductoInventarioResponse response = inventarioService.crearProducto(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/productos/{id}")
    public ResponseEntity<ProductoInventarioResponse> actualizarProducto(
            @PathVariable Long id,
            @Valid @RequestBody ProductoInventarioUpdateRequest request
    ) {
        return ResponseEntity.ok(inventarioService.actualizarProducto(id, request));
    }

    @PatchMapping("/productos/{id}/estado")
    public ResponseEntity<ProductoInventarioResponse> cambiarEstadoProducto(
            @PathVariable Long id,
            @Valid @RequestBody ProductoInventarioEstadoRequest request
    ) {
        return ResponseEntity.ok(inventarioService.cambiarEstadoProducto(id, request));
    }

    @GetMapping("/movimientos")
    public ResponseEntity<Page<MovimientoInventarioResponse>> listarMovimientos(
            @RequestParam(required = false) Long productoId,
            @RequestParam(required = false) Long ordenServicioId,
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(inventarioService.listarMovimientos(productoId, ordenServicioId, pageable));
    }

    @PostMapping("/movimientos")
    public ResponseEntity<MovimientoInventarioResponse> registrarMovimiento(@Valid @RequestBody MovimientoInventarioCreateRequest request) {
        MovimientoInventarioResponse response = inventarioService.registrarMovimiento(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
