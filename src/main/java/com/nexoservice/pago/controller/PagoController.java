package com.nexoservice.pago.controller;

import com.nexoservice.pago.dto.request.PagoAnularRequest;
import com.nexoservice.pago.dto.request.PagoCreateRequest;
import com.nexoservice.pago.dto.response.EstadoDeudaResponse;
import com.nexoservice.pago.dto.response.PagoResponse;
import com.nexoservice.pago.entity.EstadoPago;
import com.nexoservice.pago.service.PagoService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @GetMapping
    public ResponseEntity<Page<PagoResponse>> listar(
            @RequestParam(required = false) Long ordenServicioId,
            @RequestParam(required = false) EstadoPago estado,
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(pagoService.listar(ordenServicioId, estado, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.buscarPorId(id));
    }

    @GetMapping("/orden/{ordenServicioId}/deuda")
    public ResponseEntity<EstadoDeudaResponse> consultarDeudaPorOrden(@PathVariable Long ordenServicioId) {
        return ResponseEntity.ok(pagoService.consultarDeudaPorOrden(ordenServicioId));
    }

    @PostMapping
    public ResponseEntity<PagoResponse> registrar(@Valid @RequestBody PagoCreateRequest request) {
        PagoResponse response = pagoService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/anular")
    public ResponseEntity<PagoResponse> anular(
            @PathVariable Long id,
            @Valid @RequestBody PagoAnularRequest request
    ) {
        return ResponseEntity.ok(pagoService.anular(id, request));
    }
}
