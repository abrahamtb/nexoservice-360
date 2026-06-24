package com.nexoservice.web.controller;

import com.nexoservice.tecnico.dto.request.TecnicoCreateRequest;
import com.nexoservice.tecnico.dto.request.TecnicoEstadoRequest;
import com.nexoservice.tecnico.dto.request.TecnicoUpdateRequest;
import com.nexoservice.tecnico.dto.response.TecnicoResponse;
import com.nexoservice.tecnico.service.TecnicoService;
import com.nexoservice.web.form.TecnicoForm;
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
@RequestMapping("/tecnicos")
@RequiredArgsConstructor
public class TecnicoWebController {

    private final TecnicoService tecnicoService;

    @GetMapping
    public String listar(@RequestParam(required = false) String buscar, Model model) {
        Pageable pageable = PageRequest.of(0, 50);
        model.addAttribute("tecnicos", tecnicoService.listar(buscar, pageable).getContent());
        model.addAttribute("buscar", buscar);
        return "tecnicos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("tecnico", new TecnicoForm());
        return "tecnicos/form";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("tecnico") TecnicoForm form) {
        tecnicoService.crear(new TecnicoCreateRequest(
                form.getNombres(),
                form.getApellidos(),
                form.getEspecialidad(),
                form.getCorreo(),
                form.getTelefono()
        ));
        return "redirect:/tecnicos";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        TecnicoResponse response = tecnicoService.buscarPorId(id);
        model.addAttribute("tecnico", toForm(response));
        model.addAttribute("tecnicoId", id);
        return "tecnicos/form";
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id, @Valid @ModelAttribute("tecnico") TecnicoForm form) {
        tecnicoService.actualizar(id, new TecnicoUpdateRequest(
                form.getNombres(),
                form.getApellidos(),
                form.getEspecialidad(),
                form.getCorreo(),
                form.getTelefono()
        ));
        return "redirect:/tecnicos";
    }

    @PostMapping("/{id}/desactivar")
    public String desactivar(@PathVariable Long id) {
        tecnicoService.cambiarEstado(id, new TecnicoEstadoRequest(false));
        return "redirect:/tecnicos";
    }

    private TecnicoForm toForm(TecnicoResponse response) {
        TecnicoForm form = new TecnicoForm();
        form.setNombres(response.nombres());
        form.setApellidos(response.apellidos());
        form.setEspecialidad(response.especialidad());
        form.setCorreo(response.correo());
        form.setTelefono(response.telefono());
        return form;
    }
}
