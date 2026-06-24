package com.nexoservice.tipoactivo.controller;

import com.nexoservice.tipoactivo.dto.request.TipoActivoCreateRequest;
import com.nexoservice.tipoactivo.dto.request.TipoActivoEstadoRequest;
import com.nexoservice.tipoactivo.dto.request.TipoActivoUpdateRequest;
import com.nexoservice.tipoactivo.dto.response.TipoActivoResponse;
import com.nexoservice.tipoactivo.service.TipoActivoService;
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
@RequestMapping("/api/tipos-activo")
@RequiredArgsConstructor
public class TipoActivoController {

    private final TipoActivoService tipoActivoService;

    @GetMapping
    public ResponseEntity<Page<TipoActivoResponse>> listar(
            @RequestParam(required = false) String buscar,
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(tipoActivoService.listar(buscar, pageable));
    }

    @GetMapping("/activos")
    public ResponseEntity<Page<TipoActivoResponse>> listarActivos(
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(tipoActivoService.listarActivos(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoActivoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tipoActivoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<TipoActivoResponse> crear(@Valid @RequestBody TipoActivoCreateRequest request) {
        TipoActivoResponse response = tipoActivoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoActivoResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody TipoActivoUpdateRequest request
    ) {
        return ResponseEntity.ok(tipoActivoService.actualizar(id, request));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<TipoActivoResponse> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody TipoActivoEstadoRequest request
    ) {
        return ResponseEntity.ok(tipoActivoService.cambiarEstado(id, request));
    }
}
