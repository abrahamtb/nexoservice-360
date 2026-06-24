package com.nexoservice.mantenimiento.controller;

import com.nexoservice.mantenimiento.dto.request.MantenimientoProgramadoCreateRequest;
import com.nexoservice.mantenimiento.dto.response.MantenimientoProgramadoResponse;
import com.nexoservice.mantenimiento.entity.EstadoMantenimientoProgramado;
import com.nexoservice.mantenimiento.service.MantenimientoProgramadoService;
import com.nexoservice.orden.dto.response.OrdenServicioResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mantenimientos-programados")
@RequiredArgsConstructor
public class MantenimientoProgramadoController {

    private final MantenimientoProgramadoService mantenimientoService;

    @GetMapping
    public ResponseEntity<Page<MantenimientoProgramadoResponse>> listar(
            @RequestParam(required = false) EstadoMantenimientoProgramado estado,
            @RequestParam(defaultValue = "false") boolean soloAlertas,
            @PageableDefault(size = 10, sort = "proximaFecha") Pageable pageable
    ) {
        return ResponseEntity.ok(mantenimientoService.listar(estado, soloAlertas, pageable));
    }

    @PostMapping
    public ResponseEntity<MantenimientoProgramadoResponse> crear(@Valid @RequestBody MantenimientoProgramadoCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mantenimientoService.crear(request));
    }

    @PostMapping("/{id}/generar-orden")
    public ResponseEntity<OrdenServicioResponse> generarOrden(
            @PathVariable Long id,
            @RequestParam(defaultValue = "Sistema") String usuarioResponsable
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mantenimientoService.generarOrden(id, usuarioResponsable));
    }
}
