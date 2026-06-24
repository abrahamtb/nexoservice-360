package com.nexoservice.web.controller;

import com.nexoservice.diagnostico.dto.request.DiagnosticoCreateRequest;
import com.nexoservice.diagnostico.dto.request.DiagnosticoUpdateRequest;
import com.nexoservice.diagnostico.dto.response.DiagnosticoResponse;
import com.nexoservice.diagnostico.service.DiagnosticoService;
import com.nexoservice.web.form.DiagnosticoForm;
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
@RequestMapping("/diagnosticos")
@RequiredArgsConstructor
public class DiagnosticoWebController {

    private final DiagnosticoService diagnosticoService;

    @GetMapping("/orden/{ordenId}/nuevo")
    public String nuevo(@PathVariable Long ordenId, Model model) {
        DiagnosticoForm form = new DiagnosticoForm();
        form.setOrdenServicioId(ordenId);
        model.addAttribute("diagnostico", form);
        model.addAttribute("ordenId", ordenId);
        return "diagnosticos/form";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("diagnostico") DiagnosticoForm form) {
        diagnosticoService.crear(new DiagnosticoCreateRequest(
                form.getOrdenServicioId(),
                form.getDescripcionFalla(),
                form.getCausaProbable(),
                form.getSolucionRecomendada(),
                form.getObservaciones(),
                form.getUsuarioResponsable()
        ));
        return "redirect:/ordenes/" + form.getOrdenServicioId();
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        DiagnosticoResponse response = diagnosticoService.buscarPorId(id);
        model.addAttribute("diagnostico", toForm(response));
        model.addAttribute("diagnosticoId", id);
        model.addAttribute("ordenId", response.ordenServicioId());
        return "diagnosticos/form";
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id, @Valid @ModelAttribute("diagnostico") DiagnosticoForm form) {
        diagnosticoService.actualizar(id, new DiagnosticoUpdateRequest(
                form.getDescripcionFalla(),
                form.getCausaProbable(),
                form.getSolucionRecomendada(),
                form.getObservaciones(),
                form.getUsuarioResponsable()
        ));
        return "redirect:/ordenes/" + form.getOrdenServicioId();
    }

    private DiagnosticoForm toForm(DiagnosticoResponse response) {
        DiagnosticoForm form = new DiagnosticoForm();
        form.setOrdenServicioId(response.ordenServicioId());
        form.setDescripcionFalla(response.descripcionFalla());
        form.setCausaProbable(response.causaProbable());
        form.setSolucionRecomendada(response.solucionRecomendada());
        form.setObservaciones(response.observaciones());
        form.setUsuarioResponsable(response.usuarioResponsable());
        return form;
    }
}
