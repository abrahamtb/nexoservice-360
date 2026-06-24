package com.nexoservice.security;

import com.nexoservice.exception.BusinessException;
import com.nexoservice.orden.entity.EstadoOrden;
import com.nexoservice.orden.entity.OrdenServicio;
import com.nexoservice.rol.entity.NombreRol;
import com.nexoservice.usuario.entity.Usuario;
import com.nexoservice.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private static final Set<EstadoOrden> ESTADOS_TECNICOS_PERMITIDOS = Set.of(
            EstadoOrden.EN_DIAGNOSTICO,
            EstadoOrden.EN_EJECUCION,
            EstadoOrden.ESPERANDO_REPUESTO,
            EstadoOrden.FINALIZADA
    );

    private static final Set<EstadoOrden> ESTADOS_RECEPCION_PERMITIDOS = Set.of(
            EstadoOrden.LISTA_PARA_ENTREGA,
            EstadoOrden.ENTREGADA
    );

    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public Usuario usuarioActual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("No hay un usuario autenticado");
        }

        return usuarioRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AccessDeniedException("No se encontro el usuario autenticado"));
    }

    @Transactional(readOnly = true)
    public boolean esAdministrador() {
        NombreRol rol = usuarioActual().getRol().getNombre();
        return rol == NombreRol.ADMINISTRADOR || rol == NombreRol.ADMIN;
    }

    @Transactional(readOnly = true)
    public boolean esRecepcionista() {
        return usuarioActual().getRol().getNombre() == NombreRol.RECEPCIONISTA;
    }

    @Transactional(readOnly = true)
    public boolean esTecnico() {
        return usuarioActual().getRol().getNombre() == NombreRol.TECNICO;
    }

    @Transactional(readOnly = true)
    public Long tecnicoActualIdObligatorio() {
        Usuario usuario = usuarioActual();

        if (usuario.getTecnico() == null) {
            throw new AccessDeniedException("El usuario tecnico no esta vinculado a un tecnico del sistema");
        }

        return usuario.getTecnico().getId();
    }

    @Transactional(readOnly = true)
    public void validarOrdenAsignadaAlTecnicoActual(OrdenServicio orden) {
        if (!esTecnico()) {
            return;
        }

        Long tecnicoId = tecnicoActualIdObligatorio();

        if (orden.getTecnico() == null || !tecnicoId.equals(orden.getTecnico().getId())) {
            throw new AccessDeniedException("El tecnico solo puede acceder a ordenes asignadas a su usuario");
        }
    }

    @Transactional(readOnly = true)
    public void validarCambioEstadoTecnico(OrdenServicio orden, EstadoOrden estadoNuevo) {
        if (!esTecnico()) {
            return;
        }

        validarOrdenAsignadaAlTecnicoActual(orden);

        if (!ESTADOS_TECNICOS_PERMITIDOS.contains(estadoNuevo)) {
            throw new BusinessException("El tecnico no puede cambiar la orden al estado " + estadoNuevo);
        }
    }

    @Transactional(readOnly = true)
    public void validarCambioEstadoPorRol(OrdenServicio orden, EstadoOrden estadoNuevo) {
        if (esAdministrador()) {
            return;
        }

        if (esTecnico()) {
            validarCambioEstadoTecnico(orden, estadoNuevo);
            return;
        }

        if (esRecepcionista()) {
            if (!ESTADOS_RECEPCION_PERMITIDOS.contains(estadoNuevo)) {
                throw new BusinessException("Recepcion solo puede registrar la entrega de una orden finalizada");
            }
            return;
        }

        throw new AccessDeniedException("El usuario no tiene permisos para cambiar estados de ordenes");
    }

    @Transactional(readOnly = true)
    public boolean puedeCrearOrdenes() {
        return esAdministrador() || esRecepcionista();
    }

    @Transactional(readOnly = true)
    public boolean puedeGestionarDiagnosticos() {
        return esAdministrador() || esTecnico();
    }
}
