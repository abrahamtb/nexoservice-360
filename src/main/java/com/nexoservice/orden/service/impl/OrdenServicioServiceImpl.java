package com.nexoservice.orden.service.impl;

import com.nexoservice.cliente.entity.Cliente;
import com.nexoservice.cliente.repository.ClienteRepository;
import com.nexoservice.equipo.entity.Equipo;
import com.nexoservice.equipo.entity.EstadoEquipo;
import com.nexoservice.equipo.repository.EquipoRepository;
import com.nexoservice.exception.BusinessException;
import com.nexoservice.exception.ResourceNotFoundException;
import com.nexoservice.historial.dto.response.HistorialOrdenResponse;
import com.nexoservice.historial.entity.HistorialOrden;
import com.nexoservice.historial.mapper.HistorialOrdenMapper;
import com.nexoservice.historial.repository.HistorialOrdenRepository;
import com.nexoservice.orden.dto.request.AsignarTecnicoRequest;
import com.nexoservice.orden.dto.request.CambiarEstadoOrdenRequest;
import com.nexoservice.orden.dto.request.OrdenServicioCreateRequest;
import com.nexoservice.orden.dto.request.OrdenServicioUpdateRequest;
import com.nexoservice.orden.dto.response.OrdenServicioResponse;
import com.nexoservice.orden.entity.EstadoOrden;
import com.nexoservice.orden.entity.OrdenServicio;
import com.nexoservice.orden.mapper.OrdenServicioMapper;
import com.nexoservice.orden.repository.OrdenServicioRepository;
import com.nexoservice.orden.service.OrdenServicioService;
import com.nexoservice.security.AuthorizationService;
import com.nexoservice.servicio.entity.Servicio;
import com.nexoservice.servicio.repository.ServicioRepository;
import com.nexoservice.tecnico.entity.DisponibilidadTecnico;
import com.nexoservice.tecnico.entity.Tecnico;
import com.nexoservice.tecnico.repository.TecnicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrdenServicioServiceImpl implements OrdenServicioService {

    private static final Map<EstadoOrden, Set<EstadoOrden>> TRANSICIONES_PERMITIDAS = crearTransicionesPermitidas();

    private final OrdenServicioRepository ordenServicioRepository;
    private final HistorialOrdenRepository historialOrdenRepository;
    private final ClienteRepository clienteRepository;
    private final EquipoRepository equipoRepository;
    private final ServicioRepository servicioRepository;
    private final TecnicoRepository tecnicoRepository;
    private final OrdenServicioMapper ordenServicioMapper;
    private final HistorialOrdenMapper historialOrdenMapper;
    private final AuthorizationService authorizationService;

    @Override
    @Transactional(readOnly = true)
    public Page<OrdenServicioResponse> listar(EstadoOrden estado, Long clienteId, Long tecnicoId, Pageable pageable) {
        Page<OrdenServicio> ordenes;

        if (authorizationService.esTecnico()) {
            Long tecnicoActualId = authorizationService.tecnicoActualIdObligatorio();
            ordenes = estado == null
                    ? ordenServicioRepository.findByTecnicoId(tecnicoActualId, pageable)
                    : ordenServicioRepository.findByTecnicoIdAndEstado(tecnicoActualId, estado, pageable);
        } else if (estado != null) {
            ordenes = ordenServicioRepository.findByEstado(estado, pageable);
        } else if (clienteId != null) {
            ordenes = ordenServicioRepository.findByClienteId(clienteId, pageable);
        } else if (tecnicoId != null) {
            ordenes = ordenServicioRepository.findByTecnicoId(tecnicoId, pageable);
        } else {
            ordenes = ordenServicioRepository.findAll(pageable);
        }

        return ordenes.map(ordenServicioMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public OrdenServicioResponse buscarPorId(Long id) {
        OrdenServicio orden = obtenerOrden(id);
        authorizationService.validarOrdenAsignadaAlTecnicoActual(orden);
        return ordenServicioMapper.toResponse(orden);
    }

    @Override
    @Transactional
    public OrdenServicioResponse crear(OrdenServicioCreateRequest request) {
        Cliente cliente = obtenerCliente(request.clienteId());
        Equipo equipo = obtenerEquipo(request.equipoId());
        Servicio servicio = obtenerServicio(request.servicioId());
        Tecnico tecnico = request.tecnicoId() == null ? null : obtenerTecnico(request.tecnicoId());

        validarClienteActivo(cliente);
        validarEquipoPerteneceAlCliente(equipo, cliente);
        validarServicioActivo(servicio);

        if (tecnico != null) {
            validarTecnicoAsignable(tecnico);
        }

        OrdenServicio orden = ordenServicioMapper.toEntity(request, cliente, equipo, servicio, tecnico);

        if (tecnico != null) {
            orden.setEstado(EstadoOrden.ASIGNADA);
            tecnico.setDisponibilidad(DisponibilidadTecnico.OCUPADO);
        }

        OrdenServicio ordenGuardada = ordenServicioRepository.save(orden);
        registrarHistorial(ordenGuardada, null, ordenGuardada.getEstado(), request.usuarioResponsable(), "Orden creada");

        return ordenServicioMapper.toResponse(ordenGuardada);
    }

    @Override
    @Transactional
    public OrdenServicioResponse actualizar(Long id, OrdenServicioUpdateRequest request) {
        OrdenServicio orden = obtenerOrden(id);
        authorizationService.validarOrdenAsignadaAlTecnicoActual(orden);
        validarOrdenEditable(orden);

        ordenServicioMapper.updateEntity(orden, request);

        return ordenServicioMapper.toResponse(orden);
    }

    @Override
    @Transactional
    public OrdenServicioResponse asignarTecnico(Long id, AsignarTecnicoRequest request) {
        OrdenServicio orden = obtenerOrden(id);
        authorizationService.validarOrdenAsignadaAlTecnicoActual(orden);
        validarOrdenNoCerrada(orden);

        Tecnico tecnico = obtenerTecnico(request.tecnicoId());
        Tecnico tecnicoAnterior = orden.getTecnico();

        if (tecnicoAnterior != null && tecnicoAnterior.getId().equals(tecnico.getId())) {
            throw new BusinessException("El tecnico indicado ya esta asignado a la orden");
        }

        validarTecnicoAsignable(tecnico);

        EstadoOrden estadoAnterior = orden.getEstado();
        orden.setTecnico(tecnico);

        if (estadoAnterior == EstadoOrden.RECIBIDA) {
            orden.setEstado(EstadoOrden.ASIGNADA);
        }

        if (tecnicoAnterior != null) {
            tecnicoAnterior.setDisponibilidad(DisponibilidadTecnico.DISPONIBLE);
        }

        tecnico.setDisponibilidad(DisponibilidadTecnico.OCUPADO);
        registrarHistorial(orden, estadoAnterior, orden.getEstado(), request.usuarioResponsable(), request.observacion());

        return ordenServicioMapper.toResponse(orden);
    }

    @Override
    @Transactional
    public OrdenServicioResponse cambiarEstado(Long id, CambiarEstadoOrdenRequest request) {
        OrdenServicio orden = obtenerOrden(id);
        EstadoOrden estadoAnterior = orden.getEstado();
        EstadoOrden estadoNuevo = request.estadoNuevo();

        authorizationService.validarCambioEstadoPorRol(orden, estadoNuevo);
        validarTransicion(estadoAnterior, estadoNuevo);
        validarRequisitosPorEstado(orden, estadoNuevo);

        orden.setEstado(estadoNuevo);
        actualizarRelacionadosPorEstado(orden, estadoNuevo);
        registrarHistorial(orden, estadoAnterior, estadoNuevo, request.usuarioResponsable(), request.observacion());

        return ordenServicioMapper.toResponse(orden);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialOrdenResponse> listarHistorial(Long id) {
        OrdenServicio orden = obtenerOrden(id);
        authorizationService.validarOrdenAsignadaAlTecnicoActual(orden);

        return historialOrdenRepository.findByOrdenServicioIdOrderByFechaCambioAsc(id)
                .stream()
                .map(historialOrdenMapper::toResponse)
                .toList();
    }

    private OrdenServicio obtenerOrden(Long id) {
        return ordenServicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro la orden de servicio con id " + id));
    }

    private Cliente obtenerCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el cliente con id " + id));
    }

    private Equipo obtenerEquipo(Long id) {
        return equipoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el equipo con id " + id));
    }

    private Servicio obtenerServicio(Long id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el servicio con id " + id));
    }

    private Tecnico obtenerTecnico(Long id) {
        return tecnicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el tecnico con id " + id));
    }

    private void validarClienteActivo(Cliente cliente) {
        if (!Boolean.TRUE.equals(cliente.getActivo())) {
            throw new BusinessException("No se puede crear una orden para un cliente inactivo");
        }
    }

    private void validarEquipoPerteneceAlCliente(Equipo equipo, Cliente cliente) {
        if (!equipo.getCliente().getId().equals(cliente.getId())) {
            throw new BusinessException("El equipo no pertenece al cliente indicado");
        }

        if (equipo.getEstado() == EstadoEquipo.INACTIVO || equipo.getEstado() == EstadoEquipo.RETIRADO) {
            throw new BusinessException("No se puede crear una orden para un equipo inactivo o retirado");
        }
    }

    private void validarServicioActivo(Servicio servicio) {
        if (!Boolean.TRUE.equals(servicio.getActivo())) {
            throw new BusinessException("No se puede crear una orden con un servicio inactivo");
        }
    }

    private void validarTecnicoAsignable(Tecnico tecnico) {
        if (!Boolean.TRUE.equals(tecnico.getActivo())) {
            throw new BusinessException("No se puede asignar un tecnico inactivo");
        }

        if (tecnico.getDisponibilidad() != DisponibilidadTecnico.DISPONIBLE) {
            throw new BusinessException("El tecnico no esta disponible para asignacion");
        }
    }

    private void validarOrdenEditable(OrdenServicio orden) {
        validarOrdenNoCerrada(orden);
    }

    private void validarOrdenNoCerrada(OrdenServicio orden) {
        if (orden.getEstado() == EstadoOrden.ENTREGADA || orden.getEstado() == EstadoOrden.CANCELADA) {
            throw new BusinessException("No se puede modificar una orden entregada o cancelada");
        }
    }

    private void validarTransicion(EstadoOrden estadoAnterior, EstadoOrden estadoNuevo) {
        if (estadoAnterior == estadoNuevo) {
            throw new BusinessException("La orden ya se encuentra en estado " + estadoNuevo);
        }

        Set<EstadoOrden> permitidos = TRANSICIONES_PERMITIDAS.getOrDefault(estadoAnterior, Set.of());

        if (!permitidos.contains(estadoNuevo)) {
            throw new BusinessException("No se permite cambiar la orden de " + estadoAnterior + " a " + estadoNuevo);
        }
    }

    private void validarRequisitosPorEstado(OrdenServicio orden, EstadoOrden estadoNuevo) {
        if ((estadoNuevo == EstadoOrden.ASIGNADA
                || estadoNuevo == EstadoOrden.EN_DIAGNOSTICO
                || estadoNuevo == EstadoOrden.ESPERANDO_APROBACION
                || estadoNuevo == EstadoOrden.EN_EJECUCION
                || estadoNuevo == EstadoOrden.ESPERANDO_REPUESTO
                || estadoNuevo == EstadoOrden.FINALIZADA)
                && orden.getTecnico() == null) {
            throw new BusinessException("La orden necesita un tecnico asignado para pasar a " + estadoNuevo);
        }
    }

    private void actualizarRelacionadosPorEstado(OrdenServicio orden, EstadoOrden estadoNuevo) {
        Equipo equipo = orden.getEquipo();
        Tecnico tecnico = orden.getTecnico();

        if (estadoNuevo == EstadoOrden.EN_DIAGNOSTICO || estadoNuevo == EstadoOrden.EN_EJECUCION || estadoNuevo == EstadoOrden.ESPERANDO_REPUESTO) {
            equipo.setEstado(EstadoEquipo.EN_SERVICIO);
        }

        if (estadoNuevo == EstadoOrden.FINALIZADA || estadoNuevo == EstadoOrden.LISTA_PARA_ENTREGA || estadoNuevo == EstadoOrden.ENTREGADA || estadoNuevo == EstadoOrden.CANCELADA) {
            equipo.setEstado(EstadoEquipo.ACTIVO);

            if (tecnico != null) {
                tecnico.setDisponibilidad(DisponibilidadTecnico.DISPONIBLE);
            }
        }
    }

    private void registrarHistorial(
            OrdenServicio orden,
            EstadoOrden estadoAnterior,
            EstadoOrden estadoNuevo,
            String usuarioResponsable,
            String observacion
    ) {
        HistorialOrden historial = HistorialOrden.builder()
                .ordenServicio(orden)
                .estadoAnterior(estadoAnterior)
                .estadoNuevo(estadoNuevo)
                .fechaCambio(LocalDateTime.now())
                .usuarioResponsable(usuarioResponsable)
                .observacion(observacion)
                .build();

        historialOrdenRepository.save(historial);
    }

    private static Map<EstadoOrden, Set<EstadoOrden>> crearTransicionesPermitidas() {
        Map<EstadoOrden, Set<EstadoOrden>> transiciones = new EnumMap<>(EstadoOrden.class);
        transiciones.put(EstadoOrden.REGISTRADA, Set.of(EstadoOrden.RECIBIDA, EstadoOrden.CANCELADA));
        transiciones.put(EstadoOrden.RECIBIDA, Set.of(EstadoOrden.ASIGNADA, EstadoOrden.CANCELADA));
        transiciones.put(EstadoOrden.ASIGNADA, Set.of(EstadoOrden.EN_DIAGNOSTICO, EstadoOrden.CANCELADA));
        transiciones.put(EstadoOrden.EN_DIAGNOSTICO, Set.of(EstadoOrden.ESPERANDO_APROBACION, EstadoOrden.ESPERANDO_REPUESTO, EstadoOrden.CANCELADA));
        transiciones.put(EstadoOrden.ESPERANDO_APROBACION, Set.of(EstadoOrden.EN_EJECUCION, EstadoOrden.ESPERANDO_REPUESTO, EstadoOrden.CANCELADA));
        transiciones.put(EstadoOrden.EN_EJECUCION, Set.of(EstadoOrden.ESPERANDO_REPUESTO, EstadoOrden.FINALIZADA, EstadoOrden.CANCELADA));
        transiciones.put(EstadoOrden.ESPERANDO_REPUESTO, Set.of(EstadoOrden.EN_DIAGNOSTICO, EstadoOrden.EN_EJECUCION, EstadoOrden.CANCELADA));
        transiciones.put(EstadoOrden.FINALIZADA, Set.of(EstadoOrden.LISTA_PARA_ENTREGA));
        transiciones.put(EstadoOrden.LISTA_PARA_ENTREGA, Set.of(EstadoOrden.ENTREGADA));
        transiciones.put(EstadoOrden.ENTREGADA, Set.of());
        transiciones.put(EstadoOrden.CANCELADA, Set.of());
        return transiciones;
    }
}
