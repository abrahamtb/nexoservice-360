package com.nexoservice.web.controller;

import com.nexoservice.evidencia.dto.request.EvidenciaOrdenCreateRequest;
import com.nexoservice.evidencia.entity.TipoEvidencia;
import com.nexoservice.evidencia.service.EvidenciaOrdenService;
import com.nexoservice.diagnostico.service.DiagnosticoService;
import com.nexoservice.orden.dto.request.CambiarEstadoOrdenRequest;
import com.nexoservice.orden.dto.request.OrdenServicioCreateRequest;
import com.nexoservice.orden.entity.EstadoOrden;
import com.nexoservice.orden.entity.PrioridadOrden;
import com.nexoservice.orden.service.OrdenServicioService;
import com.nexoservice.security.AuthorizationService;
import com.nexoservice.trabajoorden.service.TrabajoOrdenService;
import com.nexoservice.web.form.CambiarEstadoOrdenForm;
import com.nexoservice.web.form.EvidenciaOrdenForm;
import com.nexoservice.web.form.OrdenServicioForm;
import com.nexoservice.web.service.WebCatalogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/ordenes")
@RequiredArgsConstructor
public class OrdenWebController {

    private static final java.util.Map<EstadoOrden, java.util.List<EstadoOrden>> TRANSICIONES_WEB = java.util.Map.of(
            EstadoOrden.RECIBIDA, java.util.List.of(EstadoOrden.ASIGNADA, EstadoOrden.CANCELADA),
            EstadoOrden.ASIGNADA, java.util.List.of(EstadoOrden.EN_DIAGNOSTICO, EstadoOrden.CANCELADA),
            EstadoOrden.EN_DIAGNOSTICO, java.util.List.of(EstadoOrden.ESPERANDO_APROBACION, EstadoOrden.ESPERANDO_REPUESTO, EstadoOrden.CANCELADA),
            EstadoOrden.ESPERANDO_APROBACION, java.util.List.of(EstadoOrden.EN_EJECUCION, EstadoOrden.ESPERANDO_REPUESTO, EstadoOrden.CANCELADA),
            EstadoOrden.EN_EJECUCION, java.util.List.of(EstadoOrden.ESPERANDO_REPUESTO, EstadoOrden.FINALIZADA, EstadoOrden.CANCELADA),
            EstadoOrden.ESPERANDO_REPUESTO, java.util.List.of(EstadoOrden.EN_DIAGNOSTICO, EstadoOrden.EN_EJECUCION, EstadoOrden.CANCELADA),
            EstadoOrden.FINALIZADA, java.util.List.of(EstadoOrden.LISTA_PARA_ENTREGA),
            EstadoOrden.LISTA_PARA_ENTREGA, java.util.List.of(EstadoOrden.ENTREGADA),
            EstadoOrden.ENTREGADA, java.util.List.of(),
            EstadoOrden.CANCELADA, java.util.List.of()
    );

    private static final java.util.List<EstadoOrden> ESTADOS_TECNICOS_WEB = java.util.List.of(
            EstadoOrden.EN_DIAGNOSTICO,
            EstadoOrden.EN_EJECUCION,
            EstadoOrden.ESPERANDO_REPUESTO,
            EstadoOrden.FINALIZADA
    );

    private final OrdenServicioService ordenServicioService;
    private final DiagnosticoService diagnosticoService;
    private final TrabajoOrdenService trabajoOrdenService;
    private final EvidenciaOrdenService evidenciaOrdenService;
    private final WebCatalogService webCatalogService;
    private final AuthorizationService authorizationService;

    @GetMapping
    public String listar(@RequestParam(required = false) EstadoOrden estado, Model model) {
        Pageable pageable = PageRequest.of(0, 50);
        model.addAttribute("ordenes", ordenServicioService.listar(estado, null, null, pageable).getContent());
        model.addAttribute("estados", EstadoOrden.values());
        model.addAttribute("estadoSeleccionado", estado);
        model.addAttribute("puedeCrearOrdenes", authorizationService.puedeCrearOrdenes());
        return "ordenes/lista";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        cargarCatalogos(model);
        model.addAttribute("orden", new OrdenServicioForm());
        return "ordenes/form";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("orden") OrdenServicioForm form) {
        ordenServicioService.crear(new OrdenServicioCreateRequest(
                form.getClienteId(),
                form.getEquipoId(),
                form.getServicioId(),
                form.getTecnicoId(),
                form.getPrioridad(),
                form.getFechaEstimada(),
                form.getDescripcionProblema(),
                form.getObservaciones(),
                form.getUsuarioResponsable()
        ));
        return "redirect:/ordenes";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model, Authentication authentication) {
        var orden = ordenServicioService.buscarPorId(id);
        CambiarEstadoOrdenForm cambioEstado = new CambiarEstadoOrdenForm();
        cambioEstado.setUsuarioResponsable(authentication.getName());
        EvidenciaOrdenForm evidenciaForm = new EvidenciaOrdenForm();
        evidenciaForm.setOrdenServicioId(id);
        evidenciaForm.setTecnicoId(orden.tecnicoId());
        evidenciaForm.setUsuarioResponsable(authentication.getName());

        model.addAttribute("orden", orden);
        model.addAttribute("diagnostico", diagnosticoService.buscarOpcionalPorOrden(id).orElse(null));
        model.addAttribute("trabajosOrden", trabajoOrdenService.listarPorOrden(id));
        model.addAttribute("evidencias", evidenciaOrdenService.listarPorOrden(id));
        model.addAttribute("evidencia", evidenciaForm);
        model.addAttribute("tiposEvidencia", TipoEvidencia.values());
        model.addAttribute("historial", ordenServicioService.listarHistorial(id));
        model.addAttribute("estados", obtenerEstadosPermitidos(orden.estado()));
        model.addAttribute("cambioEstado", cambioEstado);
        model.addAttribute("puedeGestionarDiagnosticos", authorizationService.puedeGestionarDiagnosticos());
        model.addAttribute("puedeGestionarTrabajos", authorizationService.puedeGestionarDiagnosticos());
        model.addAttribute("tituloCambioEstado", obtenerTituloCambioEstado());
        return "ordenes/detalle";
    }

    @PostMapping("/{id}/cambiar-estado")
    public String cambiarEstado(
            @PathVariable Long id,
            @Valid @ModelAttribute("cambioEstado") CambiarEstadoOrdenForm form
    ) {
        ordenServicioService.cambiarEstado(id, new CambiarEstadoOrdenRequest(
                form.getEstadoNuevo(),
                form.getUsuarioResponsable(),
                form.getObservacion()
        ));
        return "redirect:/ordenes/" + id;
    }

    @PostMapping("/{id}/evidencias")
    public String registrarEvidencia(
            @PathVariable Long id,
            @Valid @ModelAttribute("evidencia") EvidenciaOrdenForm form
    ) {
        evidenciaOrdenService.crear(new EvidenciaOrdenCreateRequest(
                id,
                form.getTecnicoId(),
                form.getTipo(),
                form.getNombreArchivo(),
                form.getUrlArchivo(),
                form.getObservaciones(),
                form.getUsuarioResponsable()
        ));
        return "redirect:/ordenes/" + id;
    }

    private void cargarCatalogos(Model model) {
        model.addAttribute("clientes", webCatalogService.listarClientes());
        model.addAttribute("equipos", webCatalogService.listarEquipos());
        model.addAttribute("servicios", webCatalogService.listarServiciosActivos());
        model.addAttribute("tecnicos", webCatalogService.listarTecnicosDisponibles());
        model.addAttribute("prioridades", PrioridadOrden.values());
    }

    private java.util.List<EstadoOrden> obtenerEstadosPermitidos(EstadoOrden estadoActual) {
        java.util.List<EstadoOrden> transiciones = TRANSICIONES_WEB.getOrDefault(estadoActual, java.util.List.of());

        if (authorizationService.esAdministrador()) {
            return transiciones;
        }

        if (authorizationService.esTecnico()) {
            return transiciones.stream()
                    .filter(ESTADOS_TECNICOS_WEB::contains)
                    .toList();
        }

        if (authorizationService.esRecepcionista()) {
            return transiciones.stream()
                    .filter(estado -> estado == EstadoOrden.ENTREGADA)
                    .toList();
        }

        return java.util.List.of();
    }

    private String obtenerTituloCambioEstado() {
        if (authorizationService.esTecnico()) {
            return "Actualizar avance tecnico";
        }

        if (authorizationService.esRecepcionista()) {
            return "Registrar entrega";
        }

        return "Cambiar estado";
    }
}
