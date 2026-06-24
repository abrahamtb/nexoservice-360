package com.nexoservice.evidencia.controller;

import com.nexoservice.evidencia.dto.request.EvidenciaOrdenCreateRequest;
import com.nexoservice.evidencia.dto.response.EvidenciaOrdenResponse;
import com.nexoservice.evidencia.service.EvidenciaOrdenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/evidencias")
@RequiredArgsConstructor
public class EvidenciaOrdenController {

    private final EvidenciaOrdenService evidenciaService;

    @GetMapping("/orden/{ordenServicioId}")
    public ResponseEntity<List<EvidenciaOrdenResponse>> listarPorOrden(@PathVariable Long ordenServicioId) {
        return ResponseEntity.ok(evidenciaService.listarPorOrden(ordenServicioId));
    }

    @PostMapping
    public ResponseEntity<EvidenciaOrdenResponse> crear(@Valid @RequestBody EvidenciaOrdenCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(evidenciaService.crear(request));
    }
}
