package com.nexoservice.web.form;

import com.nexoservice.evidencia.entity.TipoEvidencia;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvidenciaOrdenForm {

    private Long ordenServicioId;
    private Long tecnicoId;
    private TipoEvidencia tipo = TipoEvidencia.OBSERVACION_TECNICA;
    private String nombreArchivo;
    private String urlArchivo;
    private String observaciones;
    private String usuarioResponsable;
}
