package com.nexoservice.web.controller;

import com.nexoservice.inventario.dto.request.MovimientoInventarioCreateRequest;
import com.nexoservice.inventario.dto.request.ProductoInventarioCreateRequest;
import com.nexoservice.inventario.dto.request.ProductoInventarioEstadoRequest;
import com.nexoservice.inventario.dto.request.ProductoInventarioUpdateRequest;
import com.nexoservice.inventario.dto.response.ProductoInventarioResponse;
import com.nexoservice.inventario.entity.TipoMovimientoInventario;
import com.nexoservice.inventario.service.InventarioService;
import com.nexoservice.web.form.MovimientoInventarioForm;
import com.nexoservice.web.form.ProductoInventarioForm;
import com.nexoservice.web.service.WebCatalogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/inventario")
@RequiredArgsConstructor
public class InventarioWebController {

    private final InventarioService inventarioService;
    private final WebCatalogService webCatalogService;

    @GetMapping
    public String listar(@RequestParam(required = false) String buscar, Model model) {
        model.addAttribute("productos", inventarioService.listarProductos(buscar, PageRequest.of(0, 50)).getContent());
        model.addAttribute("buscar", buscar);
        return "inventario/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("producto", new ProductoInventarioForm());
        return "inventario/form";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("producto") ProductoInventarioForm form) {
        inventarioService.crearProducto(new ProductoInventarioCreateRequest(
                form.getCodigo(),
                form.getNombre(),
                form.getDescripcion(),
                form.getCategoria(),
                form.getUnidadMedida(),
                form.getStockInicial(),
                form.getStockMinimo(),
                form.getCostoUnitario(),
                form.getPrecioReferencia()
        ));
        return "redirect:/inventario";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        ProductoInventarioResponse producto = inventarioService.buscarProductoPorId(id);
        model.addAttribute("producto", toForm(producto));
        model.addAttribute("productoId", id);
        return "inventario/form";
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id, @Valid @ModelAttribute("producto") ProductoInventarioForm form) {
        inventarioService.actualizarProducto(id, new ProductoInventarioUpdateRequest(
                form.getCodigo(),
                form.getNombre(),
                form.getDescripcion(),
                form.getCategoria(),
                form.getUnidadMedida(),
                form.getStockMinimo(),
                form.getCostoUnitario(),
                form.getPrecioReferencia()
        ));
        return "redirect:/inventario";
    }

    @PostMapping("/{id}/desactivar")
    public String desactivar(@PathVariable Long id) {
        inventarioService.cambiarEstadoProducto(id, new ProductoInventarioEstadoRequest(false));
        return "redirect:/inventario";
    }

    @GetMapping("/movimientos")
    public String movimientos(@RequestParam(required = false) Long productoId, Model model) {
        MovimientoInventarioForm form = new MovimientoInventarioForm();
        form.setProductoId(productoId);
        model.addAttribute("movimiento", form);
        model.addAttribute("movimientos", inventarioService.listarMovimientos(productoId, null, PageRequest.of(0, 50)).getContent());
        model.addAttribute("productos", inventarioService.listarProductosActivos(PageRequest.of(0, 200)).getContent());
        model.addAttribute("ordenes", webCatalogService.listarOrdenes());
        model.addAttribute("tiposMovimiento", TipoMovimientoInventario.values());
        return "inventario/movimientos";
    }

    @PostMapping("/movimientos")
    public String registrarMovimiento(@Valid @ModelAttribute("movimiento") MovimientoInventarioForm form) {
        inventarioService.registrarMovimiento(new MovimientoInventarioCreateRequest(
                form.getProductoId(),
                form.getOrdenServicioId(),
                form.getTipoMovimiento(),
                form.getCantidad(),
                form.getMotivo(),
                form.getUsuarioResponsable()
        ));
        return "redirect:/inventario/movimientos?productoId=" + form.getProductoId();
    }

    private ProductoInventarioForm toForm(ProductoInventarioResponse response) {
        ProductoInventarioForm form = new ProductoInventarioForm();
        form.setCodigo(response.codigo());
        form.setNombre(response.nombre());
        form.setDescripcion(response.descripcion());
        form.setCategoria(response.categoria());
        form.setUnidadMedida(response.unidadMedida());
        form.setStockInicial(response.stockActual());
        form.setStockMinimo(response.stockMinimo());
        form.setCostoUnitario(response.costoUnitario());
        form.setPrecioReferencia(response.precioReferencia());
        return form;
    }
}
