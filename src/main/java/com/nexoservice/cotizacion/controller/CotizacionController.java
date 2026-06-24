package com.nexoservice.cotizacion.controller;

import com.nexoservice.cotizacion.dto.request.CotizacionCreateRequest;
import com.nexoservice.cotizacion.dto.request.CotizacionEstadoRequest;
import com.nexoservice.cotizacion.dto.request.CotizacionUpdateRequest;
import com.nexoservice.cotizacion.dto.response.CotizacionResponse;
import com.nexoservice.cotizacion.entity.EstadoCotizacion;
import com.nexoservice.cotizacion.service.CotizacionService;
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
@RequestMapping("/api/cotizaciones")
@RequiredArgsConstructor
public class CotizacionController {

    private final CotizacionService cotizacionService;

    @GetMapping
    public ResponseEntity<Page<CotizacionResponse>> listar(
            @RequestParam(required = false) EstadoCotizacion estado,
            @RequestParam(required = false) Long ordenServicioId,
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(cotizacionService.listar(estado, ordenServicioId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CotizacionResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cotizacionService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<CotizacionResponse> crear(@Valid @RequestBody CotizacionCreateRequest request) {
        CotizacionResponse response = cotizacionService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CotizacionResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CotizacionUpdateRequest request
    ) {
        return ResponseEntity.ok(cotizacionService.actualizar(id, request));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<CotizacionResponse> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CotizacionEstadoRequest request
    ) {
        return ResponseEntity.ok(cotizacionService.cambiarEstado(id, request));
    }
}
