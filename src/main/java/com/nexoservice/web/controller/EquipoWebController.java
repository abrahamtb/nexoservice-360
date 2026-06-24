package com.nexoservice.web.controller;

import com.nexoservice.equipo.dto.request.EquipoCreateRequest;
import com.nexoservice.equipo.dto.request.EquipoEstadoRequest;
import com.nexoservice.equipo.dto.request.EquipoUpdateRequest;
import com.nexoservice.equipo.dto.response.EquipoResponse;
import com.nexoservice.equipo.entity.EstadoEquipo;
import com.nexoservice.equipo.service.EquipoService;
import com.nexoservice.web.form.EquipoForm;
import com.nexoservice.web.service.WebCatalogService;
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
@RequestMapping("/equipos")
@RequiredArgsConstructor
public class EquipoWebController {

    private final EquipoService equipoService;
    private final WebCatalogService webCatalogService;

    @GetMapping
    public String listar(@RequestParam(required = false) String buscar, Model model) {
        Pageable pageable = PageRequest.of(0, 50);
        model.addAttribute("equipos", equipoService.listar(buscar, pageable).getContent());
        model.addAttribute("buscar", buscar);
        return "equipos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("equipo", new EquipoForm());
        model.addAttribute("clientes", webCatalogService.listarClientes());
        model.addAttribute("tiposActivo", webCatalogService.listarTiposActivoActivos());
        return "equipos/form";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("equipo") EquipoForm form) {
        equipoService.crear(new EquipoCreateRequest(
                form.getClienteId(),
                form.getTipoActivoId(),
                form.getTipo(),
                form.getMarca(),
                form.getModelo(),
                form.getNumeroSerie(),
                form.getCodigoInterno(),
                form.getUbicacion(),
                form.getCaracteristicas(),
                form.getDescripcion(),
                form.getObservaciones()
        ));
        return "redirect:/equipos";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        EquipoResponse response = equipoService.buscarPorId(id);
        model.addAttribute("equipo", toForm(response));
        model.addAttribute("equipoId", id);
        model.addAttribute("clientes", webCatalogService.listarClientes());
        model.addAttribute("tiposActivo", webCatalogService.listarTiposActivoActivos());
        return "equipos/form";
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id, @Valid @ModelAttribute("equipo") EquipoForm form) {
        equipoService.actualizar(id, new EquipoUpdateRequest(
                form.getTipoActivoId(),
                form.getTipo(),
                form.getMarca(),
                form.getModelo(),
                form.getNumeroSerie(),
                form.getCodigoInterno(),
                form.getUbicacion(),
                form.getCaracteristicas(),
                form.getDescripcion(),
                form.getObservaciones()
        ));
        return "redirect:/equipos";
    }

    @PostMapping("/{id}/inactivar")
    public String inactivar(@PathVariable Long id) {
        equipoService.cambiarEstado(id, new EquipoEstadoRequest(EstadoEquipo.INACTIVO));
        return "redirect:/equipos";
    }

    private EquipoForm toForm(EquipoResponse response) {
        EquipoForm form = new EquipoForm();
        form.setClienteId(response.clienteId());
        form.setTipoActivoId(response.tipoActivoId());
        form.setTipo(response.tipo());
        form.setMarca(response.marca());
        form.setModelo(response.modelo());
        form.setNumeroSerie(response.numeroSerie());
        form.setCodigoInterno(response.codigoInterno());
        form.setUbicacion(response.ubicacion());
        form.setCaracteristicas(response.caracteristicas());
        form.setDescripcion(response.descripcion());
        form.setObservaciones(response.observaciones());
        return form;
    }
}
