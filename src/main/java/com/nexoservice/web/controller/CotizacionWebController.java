package com.nexoservice.web.controller;

import com.nexoservice.cotizacion.dto.request.CotizacionCreateRequest;
import com.nexoservice.cotizacion.dto.request.CotizacionDetalleRequest;
import com.nexoservice.cotizacion.dto.request.CotizacionEstadoRequest;
import com.nexoservice.cotizacion.entity.EstadoCotizacion;
import com.nexoservice.cotizacion.service.CotizacionService;
import com.nexoservice.web.form.CotizacionForm;
import com.nexoservice.web.service.WebCatalogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cotizaciones")
@RequiredArgsConstructor
public class CotizacionWebController {

    private final CotizacionService cotizacionService;
    private final WebCatalogService webCatalogService;

    @GetMapping
    public String listar(@RequestParam(required = false) EstadoCotizacion estado, Model model) {
        model.addAttribute("cotizaciones", cotizacionService.listar(estado, null, PageRequest.of(0, 50)).getContent());
        model.addAttribute("estados", EstadoCotizacion.values());
        model.addAttribute("estadoSeleccionado", estado);
        return "cotizaciones/lista";
    }

    @GetMapping("/nueva")
    public String nueva(@RequestParam(required = false) Long ordenId, Model model) {
        CotizacionForm form = new CotizacionForm();
        form.setOrdenServicioId(ordenId);
        model.addAttribute("cotizacion", form);
        cargarCatalogos(model);
        return "cotizaciones/form";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("cotizacion") CotizacionForm form) {
        var response = cotizacionService.crear(new CotizacionCreateRequest(
                form.getOrdenServicioId(),
                form.getFechaVencimiento(),
                form.getObservaciones(),
                detalles(form)
        ));
        return "redirect:/cotizaciones/" + response.id();
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("cotizacion", cotizacionService.buscarPorId(id));
        model.addAttribute("estados", EstadoCotizacion.values());
        return "cotizaciones/detalle";
    }

    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable Long id, @RequestParam EstadoCotizacion estado) {
        cotizacionService.cambiarEstado(id, new CotizacionEstadoRequest(estado));
        return "redirect:/cotizaciones/" + id;
    }

    private void cargarCatalogos(Model model) {
        model.addAttribute("ordenes", webCatalogService.listarOrdenes());
    }

    private List<CotizacionDetalleRequest> detalles(CotizacionForm form) {
        List<CotizacionDetalleRequest> detalles = new ArrayList<>();
        agregarDetalle(detalles, form.getDescripcion1(), form.getCantidad1(), form.getPrecioUnitario1());
        agregarDetalle(detalles, form.getDescripcion2(), form.getCantidad2(), form.getPrecioUnitario2());
        agregarDetalle(detalles, form.getDescripcion3(), form.getCantidad3(), form.getPrecioUnitario3());
        return detalles;
    }

    private void agregarDetalle(List<CotizacionDetalleRequest> detalles, String descripcion, Integer cantidad, BigDecimal precioUnitario) {
        if (StringUtils.hasText(descripcion) && cantidad != null && precioUnitario != null) {
            detalles.add(new CotizacionDetalleRequest(descripcion, cantidad, precioUnitario));
        }
    }
}
