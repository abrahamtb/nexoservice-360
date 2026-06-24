package com.nexoservice.web.controller;

import com.nexoservice.tipoactivo.dto.request.TipoActivoCreateRequest;
import com.nexoservice.tipoactivo.dto.request.TipoActivoEstadoRequest;
import com.nexoservice.tipoactivo.dto.request.TipoActivoUpdateRequest;
import com.nexoservice.tipoactivo.dto.response.TipoActivoResponse;
import com.nexoservice.tipoactivo.service.TipoActivoService;
import com.nexoservice.web.form.TipoActivoForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/tipos-activo")
@RequiredArgsConstructor
public class TipoActivoWebController {

    private final TipoActivoService tipoActivoService;

    @GetMapping
    public String listar(@RequestParam(required = false) String buscar, Model model) {
        Pageable pageable = PageRequest.of(0, 50);
        model.addAttribute("tiposActivo", tipoActivoService.listar(buscar, pageable).getContent());
        model.addAttribute("buscar", buscar);
        return "tipos-activo/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("tipoActivo", new TipoActivoForm());
        return "tipos-activo/form";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("tipoActivo") TipoActivoForm form) {
        tipoActivoService.crear(new TipoActivoCreateRequest(
                form.getNombre(),
                form.getDescripcion()
        ));
        return "redirect:/tipos-activo";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        TipoActivoResponse response = tipoActivoService.buscarPorId(id);
        model.addAttribute("tipoActivo", toForm(response));
        model.addAttribute("tipoActivoId", id);
        return "tipos-activo/form";
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id, @Valid @ModelAttribute("tipoActivo") TipoActivoForm form) {
        tipoActivoService.actualizar(id, new TipoActivoUpdateRequest(
                form.getNombre(),
                form.getDescripcion()
        ));
        return "redirect:/tipos-activo";
    }

    @PostMapping("/{id}/desactivar")
    public String desactivar(@PathVariable Long id) {
        tipoActivoService.cambiarEstado(id, new TipoActivoEstadoRequest(false));
        return "redirect:/tipos-activo";
    }

    private TipoActivoForm toForm(TipoActivoResponse response) {
        TipoActivoForm form = new TipoActivoForm();
        form.setNombre(response.nombre());
        form.setDescripcion(response.descripcion());
        return form;
    }
}
