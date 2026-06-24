package com.nexoservice.web.service;

import com.nexoservice.cliente.entity.Cliente;
import com.nexoservice.cliente.repository.ClienteRepository;
import com.nexoservice.equipo.entity.Equipo;
import com.nexoservice.equipo.repository.EquipoRepository;
import com.nexoservice.orden.entity.OrdenServicio;
import com.nexoservice.orden.repository.OrdenServicioRepository;
import com.nexoservice.servicio.entity.Servicio;
import com.nexoservice.servicio.repository.ServicioRepository;
import com.nexoservice.tecnico.entity.DisponibilidadTecnico;
import com.nexoservice.tecnico.entity.Tecnico;
import com.nexoservice.tecnico.repository.TecnicoRepository;
import com.nexoservice.tipoactivo.entity.TipoActivo;
import com.nexoservice.tipoactivo.repository.TipoActivoRepository;
import com.nexoservice.web.dto.OrdenCatalogoOption;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WebCatalogService {

    private final ClienteRepository clienteRepository;
    private final EquipoRepository equipoRepository;
    private final OrdenServicioRepository ordenServicioRepository;
    private final ServicioRepository servicioRepository;
    private final TecnicoRepository tecnicoRepository;
    private final TipoActivoRepository tipoActivoRepository;

    @Transactional(readOnly = true)
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll(Sort.by("nombres").ascending());
    }

    @Transactional(readOnly = true)
    public List<Equipo> listarEquipos() {
        return equipoRepository.findAll(Sort.by("id").ascending());
    }

    @Transactional(readOnly = true)
    public List<OrdenCatalogoOption> listarOrdenes() {
        return ordenServicioRepository.findAll(Sort.by("id").descending())
                .stream()
                .map(this::toOrdenOption)
                .toList();
    }

    private OrdenCatalogoOption toOrdenOption(OrdenServicio orden) {
        String clienteNombre = orden.getCliente().getApellidos() == null || orden.getCliente().getApellidos().isBlank()
                ? orden.getCliente().getNombres()
                : orden.getCliente().getNombres() + " " + orden.getCliente().getApellidos();
        String modelo = orden.getEquipo().getModelo() == null || orden.getEquipo().getModelo().isBlank()
                ? ""
                : " " + orden.getEquipo().getModelo();
        String equipoResumen = orden.getEquipo().getTipo() + " " + orden.getEquipo().getMarca() + modelo;
        return new OrdenCatalogoOption(orden.getId(), clienteNombre, equipoResumen, String.valueOf(orden.getEstado()));
    }

    @Transactional(readOnly = true)
    public List<TipoActivo> listarTiposActivoActivos() {
        return tipoActivoRepository.findByActivoTrue(PageRequest.of(0, 100)).getContent();
    }

    @Transactional(readOnly = true)
    public List<Servicio> listarServiciosActivos() {
        return servicioRepository.findByActivoTrue(PageRequest.of(0, 100)).getContent();
    }

    @Transactional(readOnly = true)
    public List<Tecnico> listarTecnicosDisponibles() {
        return tecnicoRepository.findByActivoTrueAndDisponibilidad(DisponibilidadTecnico.DISPONIBLE, PageRequest.of(0, 100)).getContent();
    }

    @Transactional(readOnly = true)
    public List<Tecnico> listarTecnicos() {
        return tecnicoRepository.findAll(Sort.by("nombres").ascending());
    }
}
