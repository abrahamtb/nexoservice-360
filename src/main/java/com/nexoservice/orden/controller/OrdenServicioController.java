package com.nexoservice.orden.controller;

import com.nexoservice.historial.dto.response.HistorialOrdenResponse;
import com.nexoservice.orden.dto.request.AsignarTecnicoRequest;
import com.nexoservice.orden.dto.request.CambiarEstadoOrdenRequest;
import com.nexoservice.orden.dto.request.OrdenServicioCreateRequest;
import com.nexoservice.orden.dto.request.OrdenServicioUpdateRequest;
import com.nexoservice.orden.dto.response.OrdenServicioResponse;
import com.nexoservice.orden.entity.EstadoOrden;
import com.nexoservice.orden.service.OrdenServicioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
@RequiredArgsConstructor
public class OrdenServicioController {

    private final OrdenServicioService ordenServicioService;

    @GetMapping
    public ResponseEntity<Page<OrdenServicioResponse>> listar(
            @RequestParam(required = false) EstadoOrden estado,
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) Long tecnicoId,
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(ordenServicioService.listar(estado, clienteId, tecnicoId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenServicioResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ordenServicioService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<OrdenServicioResponse> crear(@Valid @RequestBody OrdenServicioCreateRequest request) {
        OrdenServicioResponse response = ordenServicioService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrdenServicioResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody OrdenServicioUpdateRequest request
    ) {
        return ResponseEntity.ok(ordenServicioService.actualizar(id, request));
    }

    @PatchMapping("/{id}/asignar-tecnico")
    public ResponseEntity<OrdenServicioResponse> asignarTecnico(
            @PathVariable Long id,
            @Valid @RequestBody AsignarTecnicoRequest request
    ) {
        return ResponseEntity.ok(ordenServicioService.asignarTecnico(id, request));
    }

    @PatchMapping("/{id}/cambiar-estado")
    public ResponseEntity<OrdenServicioResponse> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambiarEstadoOrdenRequest request
    ) {
        return ResponseEntity.ok(ordenServicioService.cambiarEstado(id, request));
    }

    @GetMapping("/{id}/historial")
    public ResponseEntity<List<HistorialOrdenResponse>> listarHistorial(@PathVariable Long id) {
        return ResponseEntity.ok(ordenServicioService.listarHistorial(id));
    }
}
