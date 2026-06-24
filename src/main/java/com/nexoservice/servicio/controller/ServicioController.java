package com.nexoservice.servicio.controller;

import com.nexoservice.servicio.dto.request.ServicioCreateRequest;
import com.nexoservice.servicio.dto.request.ServicioEstadoRequest;
import com.nexoservice.servicio.dto.request.ServicioUpdateRequest;
import com.nexoservice.servicio.dto.response.ServicioResponse;
import com.nexoservice.servicio.service.ServicioService;
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

@RestController
@RequestMapping("/api/servicios")
@RequiredArgsConstructor
public class ServicioController {

    private final ServicioService servicioService;

    @GetMapping
    public ResponseEntity<Page<ServicioResponse>> listar(
            @RequestParam(required = false) String buscar,
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(servicioService.listar(buscar, pageable));
    }

    @GetMapping("/activos")
    public ResponseEntity<Page<ServicioResponse>> listarActivos(
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(servicioService.listarActivos(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(servicioService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ServicioResponse> crear(@Valid @RequestBody ServicioCreateRequest request) {
        ServicioResponse response = servicioService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicioResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ServicioUpdateRequest request
    ) {
        return ResponseEntity.ok(servicioService.actualizar(id, request));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ServicioResponse> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody ServicioEstadoRequest request
    ) {
        return ResponseEntity.ok(servicioService.cambiarEstado(id, request));
    }
}
