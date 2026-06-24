package com.nexoservice.reporte.service;

import com.nexoservice.cotizacion.entity.Cotizacion;
import com.nexoservice.cotizacion.entity.CotizacionDetalle;
import com.nexoservice.cotizacion.repository.CotizacionRepository;
import com.nexoservice.exception.ResourceNotFoundException;
import com.nexoservice.cliente.entity.Cliente;
import com.nexoservice.cliente.repository.ClienteRepository;
import com.nexoservice.orden.entity.OrdenServicio;
import com.nexoservice.orden.repository.OrdenServicioRepository;
import com.nexoservice.pago.entity.Pago;
import com.nexoservice.pago.repository.PagoRepository;
import com.nexoservice.reporte.dto.ClienteReporteRow;
import com.nexoservice.reporte.dto.CotizacionDetalleReporteRow;
import com.nexoservice.reporte.dto.OrdenReporteRow;
import com.nexoservice.reporte.dto.ReciboPagoReporteRow;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportePdfService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DecimalFormat MONEY_FORMATTER = new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.US));

    private final ClienteRepository clienteRepository;
    private final OrdenServicioRepository ordenServicioRepository;
    private final CotizacionRepository cotizacionRepository;
    private final PagoRepository pagoRepository;

    @Transactional(readOnly = true)
    public byte[] generarReporteClientes() {
        List<Cliente> clientes = clienteRepository.findAll(Sort.by("id").ascending());
        List<ClienteReporteRow> rows = clientes
                .stream()
                .map(this::toClienteRow)
                .toList();

        long activos = clientes.stream()
                .filter(cliente -> Boolean.TRUE.equals(cliente.getActivo()))
                .count();
        Map<String, Object> params = new HashMap<>();
        params.put("totalClientes", String.valueOf(clientes.size()));
        params.put("clientesActivos", String.valueOf(activos));
        params.put("clientesInactivos", String.valueOf(clientes.size() - activos));

        return generarPdf("reports/jasper/clientes.jrxml", rows, params);
    }

    @Transactional(readOnly = true)
    public byte[] generarReporteOrdenes() {
        List<OrdenServicio> ordenes = ordenServicioRepository.findAll(Sort.by("id").ascending());
        List<OrdenReporteRow> rows = ordenes
                .stream()
                .map(this::toOrdenRow)
                .toList();

        Map<String, Object> params = new HashMap<>();
        params.put("totalOrdenes", String.valueOf(ordenes.size()));
        params.put("ordenesPendientes", String.valueOf(ordenes.stream()
                .filter(orden -> orden.getEstado() != null
                        && orden.getEstado() != com.nexoservice.orden.entity.EstadoOrden.FINALIZADA
                        && orden.getEstado() != com.nexoservice.orden.entity.EstadoOrden.ENTREGADA
                        && orden.getEstado() != com.nexoservice.orden.entity.EstadoOrden.CANCELADA)
                .count()));
        params.put("ordenesFinalizadas", String.valueOf(ordenes.stream()
                .filter(orden -> orden.getEstado() == com.nexoservice.orden.entity.EstadoOrden.FINALIZADA
                        || orden.getEstado() == com.nexoservice.orden.entity.EstadoOrden.ENTREGADA)
                .count()));
        params.put("ordenesCanceladas", String.valueOf(ordenes.stream()
                .filter(orden -> orden.getEstado() == com.nexoservice.orden.entity.EstadoOrden.CANCELADA)
                .count()));

        return generarPdf("reports/jasper/ordenes.jrxml", rows, params);
    }

    @Transactional(readOnly = true)
    public byte[] generarCotizacion(Long id) {
        Cotizacion cotizacion = cotizacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro la cotizacion con id " + id));

        List<CotizacionDetalleReporteRow> rows = cotizacion.getDetalles().stream()
                .map(this::toCotizacionDetalleRow)
                .toList();

        return generarPdf("reports/jasper/cotizacion.jrxml", rows, parametrosCotizacion(cotizacion));
    }

    @Transactional(readOnly = true)
    public byte[] generarReciboPago(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el pago con id " + id));

        return generarPdf("reports/jasper/recibo-pago.jrxml", reciboRows(pago), parametrosReciboPago(pago));
    }

    private byte[] generarPdf(String templatePath, List<?> rows, Map<String, Object> parametros) {
        try (InputStream inputStream = new ClassPathResource(templatePath).getInputStream()) {
            JasperReport report = JasperCompileManager.compileReport(inputStream);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(rows);
            Map<String, Object> params = parametrosBase();
            params.putAll(parametros);
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, params, dataSource);
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (IOException | JRException exception) {
            log.error("No se pudo generar el reporte Jasper PDF con plantilla {}", templatePath, exception);
            throw new IllegalStateException("No se pudo generar el reporte Jasper PDF", exception);
        }
    }

    private Map<String, Object> parametrosBase() {
        Map<String, Object> params = new HashMap<>();
        params.put("generatedAt", LocalDateTime.now().format(FORMATTER));
        return params;
    }

    private ClienteReporteRow toClienteRow(Cliente cliente) {
        return new ClienteReporteRow(
                cliente.getId(),
                nombreCliente(cliente),
                cliente.getTipoDocumento() + " " + cliente.getNumeroDocumento(),
                texto(cliente.getCorreo()),
                texto(cliente.getTelefono()),
                texto(cliente.getDireccion()),
                Boolean.TRUE.equals(cliente.getActivo()) ? "Activo" : "Inactivo",
                fechaHora(cliente.getFechaCreacion())
        );
    }

    private OrdenReporteRow toOrdenRow(OrdenServicio orden) {
        return new OrdenReporteRow(
                orden.getId(),
                nombreCliente(orden.getCliente()),
                resumenEquipo(orden),
                orden.getServicio().getNombre(),
                orden.getTecnico() == null ? "Sin asignar" : nombreTecnico(orden),
                enumTexto(orden.getEstado()),
                enumTexto(orden.getPrioridad()),
                fechaHora(orden.getFechaIngreso()),
                fecha(orden.getFechaEstimada())
        );
    }

    private CotizacionDetalleReporteRow toCotizacionDetalleRow(CotizacionDetalle detalle) {
        return new CotizacionDetalleReporteRow(
                detalle.getDescripcion(),
                detalle.getCantidad(),
                money(detalle.getPrecioUnitario()),
                money(detalle.getSubtotal())
        );
    }

    private Map<String, Object> parametrosCotizacion(Cotizacion cotizacion) {
        OrdenServicio orden = cotizacion.getOrdenServicio();
        Map<String, Object> params = new HashMap<>();
        params.put("numero", texto(cotizacion.getNumero()));
        params.put("cliente", nombreCliente(orden.getCliente()));
        params.put("documentoCliente", orden.getCliente().getTipoDocumento() + " " + orden.getCliente().getNumeroDocumento());
        params.put("ordenId", String.valueOf(orden.getId()));
        params.put("activo", resumenEquipo(orden));
        params.put("servicio", orden.getServicio().getNombre());
        params.put("estado", enumTexto(cotizacion.getEstado()));
        params.put("fechaEmision", fecha(cotizacion.getFechaEmision()));
        params.put("fechaVencimiento", fecha(cotizacion.getFechaVencimiento()));
        params.put("subtotal", money(cotizacion.getSubtotal()));
        params.put("impuesto", money(cotizacion.getImpuesto()));
        params.put("total", money(cotizacion.getTotal()));
        params.put("observaciones", texto(cotizacion.getObservaciones()));
        return params;
    }

    private Map<String, Object> parametrosReciboPago(Pago pago) {
        OrdenServicio orden = pago.getOrdenServicio();
        Map<String, Object> params = new HashMap<>();
        params.put("recibo", "REC-" + String.format("%06d", pago.getId()));
        params.put("cliente", nombreCliente(orden.getCliente()));
        params.put("documentoCliente", orden.getCliente().getTipoDocumento() + " " + orden.getCliente().getNumeroDocumento());
        params.put("ordenId", String.valueOf(orden.getId()));
        params.put("cotizacion", pago.getCotizacion() == null ? "-" : texto(pago.getCotizacion().getNumero()));
        params.put("monto", money(pago.getMonto()));
        params.put("estado", enumTexto(pago.getEstado()));
        params.put("fechaPago", pago.getFechaPago().format(FORMATTER));
        return params;
    }

    private List<ReciboPagoReporteRow> reciboRows(Pago pago) {
        List<ReciboPagoReporteRow> rows = new ArrayList<>();
        rows.add(new ReciboPagoReporteRow("Metodo de pago", enumTexto(pago.getMetodoPago())));
        rows.add(new ReciboPagoReporteRow("Numero de operacion", texto(pago.getNumeroOperacion())));
        rows.add(new ReciboPagoReporteRow("Usuario responsable", texto(pago.getUsuarioResponsable())));
        rows.add(new ReciboPagoReporteRow("Observaciones", texto(pago.getObservaciones())));
        return rows;
    }

    private String nombreCliente(Cliente cliente) {
        return cliente.getApellidos() == null || cliente.getApellidos().isBlank()
                ? cliente.getNombres()
                : cliente.getNombres() + " " + cliente.getApellidos();
    }

    private String resumenEquipo(OrdenServicio orden) {
        String modelo = orden.getEquipo().getModelo() == null || orden.getEquipo().getModelo().isBlank()
                ? ""
                : " " + orden.getEquipo().getModelo();
        return orden.getEquipo().getTipo() + " " + orden.getEquipo().getMarca() + modelo;
    }

    private String nombreTecnico(OrdenServicio orden) {
        return orden.getTecnico().getNombres() + " " + orden.getTecnico().getApellidos();
    }

    private String texto(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }

    private String enumTexto(Enum<?> value) {
        if (value == null) {
            return "-";
        }
        String texto = value.name().replace('_', ' ').toLowerCase(Locale.ROOT);
        return texto.substring(0, 1).toUpperCase(Locale.ROOT) + texto.substring(1);
    }

    private String fecha(LocalDate value) {
        return value == null ? "-" : value.format(DATE_FORMATTER);
    }

    private String fechaHora(LocalDateTime value) {
        return value == null ? "-" : value.format(FORMATTER);
    }

    private String money(BigDecimal value) {
        return "S/ " + MONEY_FORMATTER.format(value == null ? BigDecimal.ZERO : value);
    }
}
