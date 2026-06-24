package com.nexoservice.web.controller;

import com.nexoservice.security.AuthorizationService;
import com.nexoservice.web.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardWebController {

    private final DashboardService dashboardService;
    private final AuthorizationService authorizationService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        var totales = dashboardService.obtenerTotales();
        boolean esAdministrador = authorizationService.esAdministrador();
        boolean esRecepcionista = authorizationService.esRecepcionista();
        boolean esTecnico = authorizationService.esTecnico();

        model.addAttribute("username", authentication.getName());
        model.addAttribute("esAdministrador", esAdministrador);
        model.addAttribute("esRecepcionista", esRecepcionista);
        model.addAttribute("esTecnico", esTecnico);
        model.addAttribute("tituloDashboard", obtenerTitulo(esAdministrador, esRecepcionista, esTecnico));
        model.addAttribute("mensajeDashboard", obtenerMensaje(esAdministrador, esRecepcionista, esTecnico, authentication.getName()));
        model.addAttribute("totales", totales);
        model.addAttribute("totalOrdenes", totales.getOrDefault(esTecnico ? "Mis ordenes" : "Ordenes", 0L));
        model.addAttribute("ordenesPorEstado", dashboardService.obtenerOrdenesPorEstado());
        return "dashboard/index";
    }

    private String obtenerTitulo(boolean esAdministrador, boolean esRecepcionista, boolean esTecnico) {
        if (esAdministrador) {
            return "Panel administrativo";
        }

        if (esRecepcionista) {
            return "Recepcion y atencion";
        }

        if (esTecnico) {
            return "Mis ordenes tecnicas";
        }

        return "Panel de control";
    }

    private String obtenerMensaje(boolean esAdministrador, boolean esRecepcionista, boolean esTecnico, String username) {
        if (esAdministrador) {
            return "Bienvenido, " + username + ". Supervisa usuarios, ordenes, tecnicos, servicios y reportes del sistema.";
        }

        if (esRecepcionista) {
            return "Bienvenido, " + username + ". Registra clientes, activos, recepciones y consulta el avance de las ordenes.";
        }

        if (esTecnico) {
            return "Bienvenido, " + username + ". Aqui veras solo las ordenes asignadas a tu usuario y sus estados tecnicos.";
        }

        return "Bienvenido, " + username + ". Revisa el estado de los servicios tecnicos.";
    }
}
