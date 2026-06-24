package com.nexoservice.inventario.service.impl;

import com.nexoservice.exception.BusinessException;
import com.nexoservice.exception.DuplicateResourceException;
import com.nexoservice.exception.ResourceNotFoundException;
import com.nexoservice.inventario.dto.request.MovimientoInventarioCreateRequest;
import com.nexoservice.inventario.dto.request.ProductoInventarioCreateRequest;
import com.nexoservice.inventario.dto.request.ProductoInventarioEstadoRequest;
import com.nexoservice.inventario.dto.request.ProductoInventarioUpdateRequest;
import com.nexoservice.inventario.dto.response.MovimientoInventarioResponse;
import com.nexoservice.inventario.dto.response.ProductoInventarioResponse;
import com.nexoservice.inventario.entity.MovimientoInventario;
import com.nexoservice.inventario.entity.ProductoInventario;
import com.nexoservice.inventario.entity.TipoMovimientoInventario;
import com.nexoservice.inventario.mapper.InventarioMapper;
import com.nexoservice.inventario.repository.MovimientoInventarioRepository;
import com.nexoservice.inventario.repository.ProductoInventarioRepository;
import com.nexoservice.inventario.service.InventarioService;
import com.nexoservice.orden.entity.OrdenServicio;
import com.nexoservice.orden.repository.OrdenServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class InventarioServiceImpl implements InventarioService {

    private final ProductoInventarioRepository productoInventarioRepository;
    private final MovimientoInventarioRepository movimientoInventarioRepository;
    private final OrdenServicioRepository ordenServicioRepository;
    private final InventarioMapper inventarioMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoInventarioResponse> listarProductos(String buscar, Pageable pageable) {
        Page<ProductoInventario> productos;

        if (StringUtils.hasText(buscar)) {
            String criterio = buscar.trim();
            productos = productoInventarioRepository.findByCodigoContainingIgnoreCaseOrNombreContainingIgnoreCaseOrCategoriaContainingIgnoreCase(
                    criterio,
                    criterio,
                    criterio,
                    pageable
            );
        } else {
            productos = productoInventarioRepository.findAll(pageable);
        }

        return productos.map(inventarioMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoInventarioResponse> listarProductosActivos(Pageable pageable) {
        return productoInventarioRepository.findByActivoTrue(pageable).map(inventarioMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoInventarioResponse buscarProductoPorId(Long id) {
        return inventarioMapper.toResponse(obtenerProducto(id));
    }

    @Override
    @Transactional
    public ProductoInventarioResponse crearProducto(ProductoInventarioCreateRequest request) {
        validarCodigoDuplicado(request.codigo());

        ProductoInventario producto = inventarioMapper.toEntity(request);
        ProductoInventario productoGuardado = productoInventarioRepository.save(producto);

        if (request.stockInicial() > 0) {
            registrarMovimientoInterno(
                    productoGuardado,
                    null,
                    TipoMovimientoInventario.ENTRADA,
                    request.stockInicial(),
                    "Stock inicial",
                    "Sistema"
            );
        }

        return inventarioMapper.toResponse(productoGuardado);
    }

    @Override
    @Transactional
    public ProductoInventarioResponse actualizarProducto(Long id, ProductoInventarioUpdateRequest request) {
        ProductoInventario producto = obtenerProducto(id);
        validarCodigoDuplicadoEnOtroProducto(request.codigo(), id);

        inventarioMapper.updateEntity(producto, request);

        return inventarioMapper.toResponse(producto);
    }

    @Override
    @Transactional
    public ProductoInventarioResponse cambiarEstadoProducto(Long id, ProductoInventarioEstadoRequest request) {
        ProductoInventario producto = obtenerProducto(id);
        producto.setActivo(request.activo());
        return inventarioMapper.toResponse(producto);
    }

    @Override
    @Transactional
    public MovimientoInventarioResponse registrarMovimiento(MovimientoInventarioCreateRequest request) {
        ProductoInventario producto = obtenerProducto(request.productoId());
        OrdenServicio orden = request.ordenServicioId() == null ? null : obtenerOrden(request.ordenServicioId());

        if (!Boolean.TRUE.equals(producto.getActivo())) {
            throw new BusinessException("No se puede registrar movimientos para un producto inactivo");
        }

        MovimientoInventario movimiento = registrarMovimientoInterno(
                producto,
                orden,
                request.tipoMovimiento(),
                request.cantidad(),
                request.motivo(),
                request.usuarioResponsable()
        );

        return inventarioMapper.toResponse(movimiento);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MovimientoInventarioResponse> listarMovimientos(Long productoId, Long ordenServicioId, Pageable pageable) {
        Page<MovimientoInventario> movimientos;

        if (productoId != null) {
            movimientos = movimientoInventarioRepository.findByProductoIdOrderByFechaMovimientoDesc(productoId, pageable);
        } else if (ordenServicioId != null) {
            movimientos = movimientoInventarioRepository.findByOrdenServicioIdOrderByFechaMovimientoDesc(ordenServicioId, pageable);
        } else {
            movimientos = movimientoInventarioRepository.findAll(pageable);
        }

        return movimientos.map(inventarioMapper::toResponse);
    }

    private MovimientoInventario registrarMovimientoInterno(
            ProductoInventario producto,
            OrdenServicio orden,
            TipoMovimientoInventario tipoMovimiento,
            Integer cantidad,
            String motivo,
            String usuarioResponsable
    ) {
        Integer stockResultante = calcularStockResultante(producto, tipoMovimiento, cantidad);
        producto.setStockActual(stockResultante);

        MovimientoInventario movimiento = MovimientoInventario.builder()
                .producto(producto)
                .ordenServicio(orden)
                .tipoMovimiento(tipoMovimiento)
                .cantidad(cantidad)
                .stockResultante(stockResultante)
                .motivo(motivo)
                .usuarioResponsable(usuarioResponsable)
                .build();

        return movimientoInventarioRepository.save(movimiento);
    }

    private Integer calcularStockResultante(ProductoInventario producto, TipoMovimientoInventario tipoMovimiento, Integer cantidad) {
        int stockActual = producto.getStockActual();

        return switch (tipoMovimiento) {
            case ENTRADA -> stockActual + cantidad;
            case SALIDA -> {
                if (stockActual < cantidad) {
                    throw new BusinessException("Stock insuficiente para registrar la salida");
                }
                yield stockActual - cantidad;
            }
            case AJUSTE -> cantidad;
        };
    }

    private ProductoInventario obtenerProducto(Long id) {
        return productoInventarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el producto de inventario con id " + id));
    }

    private OrdenServicio obtenerOrden(Long id) {
        return ordenServicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro la orden de servicio con id " + id));
    }

    private void validarCodigoDuplicado(String codigo) {
        String codigoNormalizado = normalizarCodigo(codigo);

        if (productoInventarioRepository.existsByCodigoIgnoreCase(codigoNormalizado)) {
            throw new DuplicateResourceException("Ya existe un producto con el codigo " + codigoNormalizado);
        }
    }

    private void validarCodigoDuplicadoEnOtroProducto(String codigo, Long id) {
        String codigoNormalizado = normalizarCodigo(codigo);

        if (productoInventarioRepository.existsByCodigoIgnoreCaseAndIdNot(codigoNormalizado, id)) {
            throw new DuplicateResourceException("Ya existe otro producto con el codigo " + codigoNormalizado);
        }
    }

    private String normalizarCodigo(String codigo) {
        return codigo == null ? null : codigo.trim();
    }
}
