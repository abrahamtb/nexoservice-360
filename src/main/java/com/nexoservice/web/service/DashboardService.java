package com.nexoservice.web.service;

import com.nexoservice.cliente.repository.ClienteRepository;
import com.nexoservice.equipo.repository.EquipoRepository;
import com.nexoservice.cotizacion.entity.EstadoCotizacion;
import com.nexoservice.cotizacion.repository.CotizacionRepository;
import com.nexoservice.inventario.repository.ProductoInventarioRepository;
import com.nexoservice.orden.entity.EstadoOrden;
import com.nexoservice.orden.repository.OrdenServicioRepository;
import com.nexoservice.pago.entity.EstadoPago;
import com.nexoservice.pago.repository.PagoRepository;
import com.nexoservice.security.AuthorizationService;
import com.nexoservice.servicio.repository.ServicioRepository;
import com.nexoservice.tecnico.repository.TecnicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ClienteRepository clienteRepository;
    private final EquipoRepository equipoRepository;
    private final TecnicoRepository tecnicoRepository;
    private final ServicioRepository servicioRepository;
    private final OrdenServicioRepository ordenServicioRepository;
    private final ProductoInventarioRepository productoInventarioRepository;
    private final CotizacionRepository cotizacionRepository;
    private final PagoRepository pagoRepository;
    private final AuthorizationService authorizationService;

    @Transactional(readOnly = true)
    public Map<String, Long> obtenerTotales() {
        if (authorizationService.esTecnico()) {
            return obtenerTotalesTecnico();
        }

        Map<String, Long> totales = new LinkedHashMap<>();
        totales.put("Clientes", clienteRepository.count());
        totales.put("Equipos", equipoRepository.count());
        totales.put("Ordenes", ordenServicioRepository.count());

        if (authorizationService.esAdministrador()) {
            totales.put("Tecnicos", tecnicoRepository.count());
            totales.put("Servicios", servicioRepository.count());
            totales.put("Productos", productoInventarioRepository.count());
            totales.put("Stock bajo", productoInventarioRepository.countProductosConStockBajo());
            totales.put("Cotizaciones pendientes", cotizacionRepository.findByEstado(EstadoCotizacion.PENDIENTE, org.springframework.data.domain.PageRequest.of(0, 1)).getTotalElements());
            totales.put("Pagos registrados", pagoRepository.countByEstado(EstadoPago.REGISTRADO));
        }

        return totales;
    }

    @Transactional(readOnly = true)
    public Map<EstadoOrden, Long> obtenerOrdenesPorEstado() {
        if (authorizationService.esTecnico()) {
            return obtenerOrdenesPorEstadoTecnico();
        }

        Map<EstadoOrden, Long> estados = new LinkedHashMap<>();

        for (EstadoOrden estado : EstadoOrden.values()) {
            estados.put(estado, ordenServicioRepository.countByEstado(estado));
        }

        return estados;
    }

    private Map<String, Long> obtenerTotalesTecnico() {
        Long tecnicoId = authorizationService.tecnicoActualIdObligatorio();
        Map<String, Long> totales = new LinkedHashMap<>();
        totales.put("Mis ordenes", ordenServicioRepository.countByTecnicoId(tecnicoId));
        totales.put("En diagnostico", ordenServicioRepository.countByTecnicoIdAndEstado(tecnicoId, EstadoOrden.EN_DIAGNOSTICO));
        totales.put("En ejecucion", ordenServicioRepository.countByTecnicoIdAndEstado(tecnicoId, EstadoOrden.EN_EJECUCION));
        totales.put("Finalizadas", ordenServicioRepository.countByTecnicoIdAndEstado(tecnicoId, EstadoOrden.FINALIZADA));
        return totales;
    }

    private Map<EstadoOrden, Long> obtenerOrdenesPorEstadoTecnico() {
        Long tecnicoId = authorizationService.tecnicoActualIdObligatorio();
        Map<EstadoOrden, Long> estados = new LinkedHashMap<>();

        for (EstadoOrden estado : EstadoOrden.values()) {
            estados.put(estado, ordenServicioRepository.countByTecnicoIdAndEstado(tecnicoId, estado));
        }

        return estados;
    }
}
