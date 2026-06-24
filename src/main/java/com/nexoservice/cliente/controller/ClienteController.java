package com.nexoservice.cliente.controller;

import com.nexoservice.cliente.dto.request.ClienteCreateRequest;
import com.nexoservice.cliente.dto.request.ClienteEstadoRequest;
import com.nexoservice.cliente.dto.request.ClienteUpdateRequest;
import com.nexoservice.cliente.dto.response.ClienteResponse;
import com.nexoservice.cliente.service.ClienteService;
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
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public ResponseEntity<Page<ClienteResponse>> listar(
            @RequestParam(required = false) String buscar,
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(clienteService.listar(buscar, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> crear(@Valid @RequestBody ClienteCreateRequest request) {
        ClienteResponse response = clienteService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteUpdateRequest request
    ) {
        return ResponseEntity.ok(clienteService.actualizar(id, request));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ClienteResponse> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody ClienteEstadoRequest request
    ) {
        return ResponseEntity.ok(clienteService.cambiarEstado(id, request));
    }
}
