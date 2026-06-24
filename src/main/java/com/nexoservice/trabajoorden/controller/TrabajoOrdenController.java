package com.nexoservice.trabajoorden.controller;

import com.nexoservice.trabajoorden.dto.request.TrabajoOrdenCreateRequest;
import com.nexoservice.trabajoorden.dto.request.TrabajoOrdenUpdateRequest;
import com.nexoservice.trabajoorden.dto.response.TrabajoOrdenResponse;
import com.nexoservice.trabajoorden.service.TrabajoOrdenService;
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

import java.util.List;

@RestController
@RequestMapping("/api/trabajos-orden")
@RequiredArgsConstructor
public class TrabajoOrdenController {

    private final TrabajoOrdenService trabajoOrdenService;

    @GetMapping("/{id}")
    public ResponseEntity<TrabajoOrdenResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(trabajoOrdenService.buscarPorId(id));
    }

    @GetMapping("/orden/{ordenServicioId}")
    public ResponseEntity<List<TrabajoOrdenResponse>> listarPorOrden(@PathVariable Long ordenServicioId) {
        return ResponseEntity.ok(trabajoOrdenService.listarPorOrden(ordenServicioId));
    }

    @PostMapping
    public ResponseEntity<TrabajoOrdenResponse> crear(@Valid @RequestBody TrabajoOrdenCreateRequest request) {
        TrabajoOrdenResponse response = trabajoOrdenService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrabajoOrdenResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody TrabajoOrdenUpdateRequest request
    ) {
        return ResponseEntity.ok(trabajoOrdenService.actualizar(id, request));
    }
}
