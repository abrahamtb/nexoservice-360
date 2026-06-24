package com.nexoservice.web.controller;

import com.nexoservice.pago.dto.request.PagoCreateRequest;
import com.nexoservice.pago.entity.EstadoPago;
import com.nexoservice.pago.entity.MetodoPago;
import com.nexoservice.pago.service.PagoService;
import com.nexoservice.web.form.PagoForm;
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
@RequestMapping("/pagos")
@RequiredArgsConstructor
public class PagoWebController {

    private final PagoService pagoService;
    private final WebCatalogService webCatalogService;

    @GetMapping
    public String listar(@RequestParam(required = false) Long ordenServicioId, Model model) {
        model.addAttribute("pagos", pagoService.listar(ordenServicioId, null, PageRequest.of(0, 50)).getContent());
        model.addAttribute("ordenServicioId", ordenServicioId);
        return "pagos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(@RequestParam(required = false) Long ordenId, @RequestParam(required = false) Long cotizacionId, Model model) {
        PagoForm form = new PagoForm();
        form.setOrdenServicioId(ordenId);
        form.setCotizacionId(cotizacionId);
        model.addAttribute("pago", form);
        cargarCatalogos(model);
        return "pagos/form";
    }

    @PostMapping
    public String registrar(@Valid @ModelAttribute("pago") PagoForm form) {
        var response = pagoService.registrar(new PagoCreateRequest(
                form.getOrdenServicioId(),
                form.getCotizacionId(),
                form.getMonto(),
                form.getMetodoPago(),
                form.getNumeroOperacion(),
                form.getObservaciones(),
                form.getUsuarioResponsable()
        ));
        return "redirect:/pagos/" + response.id();
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("pago", pagoService.buscarPorId(id));
        return "pagos/detalle";
    }

    @GetMapping("/deuda")
    public String deuda(@RequestParam Long ordenServicioId, Model model) {
        model.addAttribute("deuda", pagoService.consultarDeudaPorOrden(ordenServicioId));
        model.addAttribute("pagos", pagoService.listar(ordenServicioId, EstadoPago.REGISTRADO, PageRequest.of(0, 50)).getContent());
        return "pagos/deuda";
    }

    private void cargarCatalogos(Model model) {
        model.addAttribute("ordenes", webCatalogService.listarOrdenes());
        model.addAttribute("metodosPago", MetodoPago.values());
    }
}
