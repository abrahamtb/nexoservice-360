package com.nexoservice.web.controller;

import com.nexoservice.trabajoorden.dto.request.TrabajoOrdenCreateRequest;
import com.nexoservice.trabajoorden.dto.request.TrabajoOrdenUpdateRequest;
import com.nexoservice.trabajoorden.dto.response.TrabajoOrdenResponse;
import com.nexoservice.trabajoorden.service.TrabajoOrdenService;
import com.nexoservice.web.form.TrabajoOrdenForm;
import com.nexoservice.web.service.WebCatalogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/trabajos-orden")
@RequiredArgsConstructor
public class TrabajoOrdenWebController {

    private final TrabajoOrdenService trabajoOrdenService;
    private final WebCatalogService webCatalogService;

    @GetMapping("/orden/{ordenId}/nuevo")
    public String nuevo(@PathVariable Long ordenId, Model model) {
        TrabajoOrdenForm form = new TrabajoOrdenForm();
        form.setOrdenServicioId(ordenId);
        cargarModeloFormulario(model, form, null, ordenId);
        return "trabajos-orden/form";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("trabajoOrden") TrabajoOrdenForm form) {
        trabajoOrdenService.crear(new TrabajoOrdenCreateRequest(
                form.getOrdenServicioId(),
                form.getTecnicoId(),
                form.getActividad(),
                form.getDetalle(),
                form.getTiempoInvertidoMinutos(),
                form.getObservaciones(),
                form.getUsuarioResponsable()
        ));
        return "redirect:/ordenes/" + form.getOrdenServicioId();
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        TrabajoOrdenResponse response = trabajoOrdenService.buscarPorId(id);
        cargarModeloFormulario(model, toForm(response), id, response.ordenServicioId());
        return "trabajos-orden/form";
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id, @Valid @ModelAttribute("trabajoOrden") TrabajoOrdenForm form) {
        trabajoOrdenService.actualizar(id, new TrabajoOrdenUpdateRequest(
                form.getTecnicoId(),
                form.getActividad(),
                form.getDetalle(),
                form.getTiempoInvertidoMinutos(),
                form.getObservaciones(),
                form.getUsuarioResponsable()
        ));
        return "redirect:/ordenes/" + form.getOrdenServicioId();
    }

    private void cargarModeloFormulario(Model model, TrabajoOrdenForm form, Long trabajoOrdenId, Long ordenId) {
        model.addAttribute("trabajoOrden", form);
        model.addAttribute("trabajoOrdenId", trabajoOrdenId);
        model.addAttribute("ordenId", ordenId);
        model.addAttribute("tecnicos", webCatalogService.listarTecnicos());
    }

    private TrabajoOrdenForm toForm(TrabajoOrdenResponse response) {
        TrabajoOrdenForm form = new TrabajoOrdenForm();
        form.setOrdenServicioId(response.ordenServicioId());
        form.setTecnicoId(response.tecnicoId());
        form.setActividad(response.actividad());
        form.setDetalle(response.detalle());
        form.setTiempoInvertidoMinutos(response.tiempoInvertidoMinutos());
        form.setObservaciones(response.observaciones());
        form.setUsuarioResponsable(response.usuarioResponsable());
        return form;
    }
}
