package com.nexoservice.equipo.controller;

import com.nexoservice.equipo.dto.request.EquipoCreateRequest;
import com.nexoservice.equipo.dto.request.EquipoEstadoRequest;
import com.nexoservice.equipo.dto.request.EquipoUpdateRequest;
import com.nexoservice.equipo.dto.response.EquipoResponse;
import com.nexoservice.equipo.service.EquipoService;
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
@RequestMapping("/api/equipos")
@RequiredArgsConstructor
public class EquipoController {

    private final EquipoService equipoService;

    @GetMapping
    public ResponseEntity<Page<EquipoResponse>> listar(
            @RequestParam(required = false) String buscar,
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(equipoService.listar(buscar, pageable));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<Page<EquipoResponse>> listarPorCliente(
            @PathVariable Long clienteId,
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(equipoService.listarPorCliente(clienteId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(equipoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<EquipoResponse> crear(@Valid @RequestBody EquipoCreateRequest request) {
        EquipoResponse response = equipoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipoResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EquipoUpdateRequest request
    ) {
        return ResponseEntity.ok(equipoService.actualizar(id, request));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<EquipoResponse> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody EquipoEstadoRequest request
    ) {
        return ResponseEntity.ok(equipoService.cambiarEstado(id, request));
    }
}
