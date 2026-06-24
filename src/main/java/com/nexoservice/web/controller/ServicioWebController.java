package com.nexoservice.web.controller;

import com.nexoservice.servicio.dto.request.ServicioCreateRequest;
import com.nexoservice.servicio.dto.request.ServicioEstadoRequest;
import com.nexoservice.servicio.dto.request.ServicioUpdateRequest;
import com.nexoservice.servicio.dto.response.ServicioResponse;
import com.nexoservice.servicio.service.ServicioService;
import com.nexoservice.web.form.ServicioForm;
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
@RequestMapping("/servicios")
@RequiredArgsConstructor
public class ServicioWebController {

    private final ServicioService servicioService;

    @GetMapping
    public String listar(@RequestParam(required = false) String buscar, Model model) {
        Pageable pageable = PageRequest.of(0, 50);
        model.addAttribute("servicios", servicioService.listar(buscar, pageable).getContent());
        model.addAttribute("buscar", buscar);
        return "servicios/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("servicio", new ServicioForm());
        return "servicios/form";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("servicio") ServicioForm form) {
        servicioService.crear(new ServicioCreateRequest(
                form.getNombre(),
                form.getDescripcion(),
                form.getPrecioBase(),
                form.getDuracionEstimadaMinutos()
        ));
        return "redirect:/servicios";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        ServicioResponse response = servicioService.buscarPorId(id);
        model.addAttribute("servicio", toForm(response));
        model.addAttribute("servicioId", id);
        return "servicios/form";
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id, @Valid @ModelAttribute("servicio") ServicioForm form) {
        servicioService.actualizar(id, new ServicioUpdateRequest(
                form.getNombre(),
                form.getDescripcion(),
                form.getPrecioBase(),
                form.getDuracionEstimadaMinutos()
        ));
        return "redirect:/servicios";
    }

    @PostMapping("/{id}/desactivar")
    public String desactivar(@PathVariable Long id) {
        servicioService.cambiarEstado(id, new ServicioEstadoRequest(false));
        return "redirect:/servicios";
    }

    private ServicioForm toForm(ServicioResponse response) {
        ServicioForm form = new ServicioForm();
        form.setNombre(response.nombre());
        form.setDescripcion(response.descripcion());
        form.setPrecioBase(response.precioBase());
        form.setDuracionEstimadaMinutos(response.duracionEstimadaMinutos());
        return form;
    }
}
