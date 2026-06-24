package com.nexoservice.tecnico.controller;

import com.nexoservice.tecnico.dto.request.TecnicoCreateRequest;
import com.nexoservice.tecnico.dto.request.TecnicoDisponibilidadRequest;
import com.nexoservice.tecnico.dto.request.TecnicoEstadoRequest;
import com.nexoservice.tecnico.dto.request.TecnicoUpdateRequest;
import com.nexoservice.tecnico.dto.response.TecnicoResponse;
import com.nexoservice.tecnico.service.TecnicoService;
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
@RequestMapping("/api/tecnicos")
@RequiredArgsConstructor
public class TecnicoController {

    private final TecnicoService tecnicoService;

    @GetMapping
    public ResponseEntity<Page<TecnicoResponse>> listar(
            @RequestParam(required = false) String buscar,
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(tecnicoService.listar(buscar, pageable));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<Page<TecnicoResponse>> listarDisponibles(
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(tecnicoService.listarDisponibles(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TecnicoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tecnicoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<TecnicoResponse> crear(@Valid @RequestBody TecnicoCreateRequest request) {
        TecnicoResponse response = tecnicoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TecnicoResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody TecnicoUpdateRequest request
    ) {
        return ResponseEntity.ok(tecnicoService.actualizar(id, request));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<TecnicoResponse> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody TecnicoEstadoRequest request
    ) {
        return ResponseEntity.ok(tecnicoService.cambiarEstado(id, request));
    }

    @PatchMapping("/{id}/disponibilidad")
    public ResponseEntity<TecnicoResponse> cambiarDisponibilidad(
            @PathVariable Long id,
            @Valid @RequestBody TecnicoDisponibilidadRequest request
    ) {
        return ResponseEntity.ok(tecnicoService.cambiarDisponibilidad(id, request));
    }
}
