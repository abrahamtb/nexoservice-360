package com.nexoservice.reporte.controller;

import com.nexoservice.reporte.service.ReportePdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReportePdfService reportePdfService;

    @GetMapping("/clientes.pdf")
    public ResponseEntity<byte[]> reporteClientes() {
        return pdfResponse("reporte-clientes.pdf", reportePdfService.generarReporteClientes());
    }

    @GetMapping("/ordenes.pdf")
    public ResponseEntity<byte[]> reporteOrdenes() {
        return pdfResponse("reporte-ordenes.pdf", reportePdfService.generarReporteOrdenes());
    }

    @GetMapping("/cotizaciones/{id}.pdf")
    public ResponseEntity<byte[]> cotizacionPdf(@PathVariable Long id) {
        return pdfResponse("cotizacion-" + id + ".pdf", reportePdfService.generarCotizacion(id));
    }

    @GetMapping("/pagos/{id}.pdf")
    public ResponseEntity<byte[]> reciboPagoPdf(@PathVariable Long id) {
        return pdfResponse("recibo-pago-" + id + ".pdf", reportePdfService.generarReciboPago(id));
    }

    private ResponseEntity<byte[]> pdfResponse(String filename, byte[] content) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline().filename(filename).build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(content);
    }
}
