package com.nexoservice.web.controller;

import com.nexoservice.equipo.service.EquipoService;
import com.nexoservice.orden.entity.EstadoOrden;
import com.nexoservice.orden.service.OrdenServicioService;
import com.nexoservice.web.service.WebCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/consultas")
@RequiredArgsConstructor
public class ConsultaWebController {

    private final EquipoService equipoService;
    private final OrdenServicioService ordenServicioService;
    private final WebCatalogService webCatalogService;

    @GetMapping("/equipos-por-cliente")
    public String equiposPorCliente(@RequestParam(required = false) Long clienteId, Model model) {
        Pageable pageable = PageRequest.of(0, 50);

        model.addAttribute("clientes", webCatalogService.listarClientes());
        model.addAttribute("clienteId", clienteId);

        if (clienteId != null) {
            model.addAttribute("equipos", equipoService.listarPorCliente(clienteId, pageable).getContent());
        }

        return "consultas/equipos-por-cliente";
    }

    @GetMapping("/ordenes-por-estado")
    public String ordenesPorEstado(@RequestParam(required = false) EstadoOrden estado, Model model) {
        Pageable pageable = PageRequest.of(0, 50);

        model.addAttribute("estados", EstadoOrden.values());
        model.addAttribute("estadoSeleccionado", estado);

        if (estado != null) {
            model.addAttribute("ordenes", ordenServicioService.listar(estado, null, null, pageable).getContent());
        }

        return "consultas/ordenes-por-estado";
    }
}
