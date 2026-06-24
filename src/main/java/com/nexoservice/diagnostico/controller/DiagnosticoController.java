package com.nexoservice.diagnostico.controller;

import com.nexoservice.diagnostico.dto.request.DiagnosticoCreateRequest;
import com.nexoservice.diagnostico.dto.request.DiagnosticoUpdateRequest;
import com.nexoservice.diagnostico.dto.response.DiagnosticoResponse;
import com.nexoservice.diagnostico.service.DiagnosticoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/diagnosticos")
@RequiredArgsConstructor
public class DiagnosticoController {

    private final DiagnosticoService diagnosticoService;

    @GetMapping("/{id}")
    public ResponseEntity<DiagnosticoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(diagnosticoService.buscarPorId(id));
    }

    @GetMapping("/orden/{ordenServicioId}")
    public ResponseEntity<DiagnosticoResponse> buscarPorOrden(@PathVariable Long ordenServicioId) {
        return ResponseEntity.ok(diagnosticoService.buscarPorOrden(ordenServicioId));
    }

    @PostMapping
    public ResponseEntity<DiagnosticoResponse> crear(@Valid @RequestBody DiagnosticoCreateRequest request) {
        DiagnosticoResponse response = diagnosticoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiagnosticoResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody DiagnosticoUpdateRequest request
    ) {
        return ResponseEntity.ok(diagnosticoService.actualizar(id, request));
    }
}
