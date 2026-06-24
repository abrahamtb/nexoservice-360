package com.nexoservice.mantenimiento.service;

import com.nexoservice.mantenimiento.dto.request.MantenimientoProgramadoCreateRequest;
import com.nexoservice.mantenimiento.dto.response.MantenimientoProgramadoResponse;
import com.nexoservice.mantenimiento.entity.EstadoMantenimientoProgramado;
import com.nexoservice.orden.dto.response.OrdenServicioResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MantenimientoProgramadoService {

    Page<MantenimientoProgramadoResponse> listar(EstadoMantenimientoProgramado estado, boolean soloAlertas, Pageable pageable);

    MantenimientoProgramadoResponse crear(MantenimientoProgramadoCreateRequest request);

    OrdenServicioResponse generarOrden(Long id, String usuarioResponsable);
}
