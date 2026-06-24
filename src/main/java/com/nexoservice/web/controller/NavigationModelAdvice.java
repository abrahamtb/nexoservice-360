package com.nexoservice.web.controller;

import com.nexoservice.rol.entity.NombreRol;
import com.nexoservice.usuario.entity.Usuario;
import com.nexoservice.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class NavigationModelAdvice {

    private final UsuarioRepository usuarioRepository;

    @ModelAttribute
    public void agregarPermisosDeNavegacion(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            agregarPermisos(model, false, false, false);
            return;
        }

        usuarioRepository.findByUsername(authentication.getName())
                .map(Usuario::getRol)
                .map(rol -> rol.getNombre())
                .ifPresentOrElse(
                        rol -> agregarPermisos(
                                model,
                                rol == NombreRol.ADMINISTRADOR || rol == NombreRol.ADMIN,
                                rol == NombreRol.RECEPCIONISTA,
                                rol == NombreRol.TECNICO
                        ),
                        () -> agregarPermisos(model, false, false, false)
                );
    }

    private void agregarPermisos(Model model, boolean esAdministrador, boolean esRecepcionista, boolean esTecnico) {
        boolean puedeCrearOrdenes = esAdministrador || esRecepcionista;
        boolean puedeGestionarClientes = esAdministrador || esRecepcionista;
        boolean puedeGestionarEquipos = esAdministrador || esRecepcionista;

        model.addAttribute("navEsAdministrador", esAdministrador);
        model.addAttribute("navEsRecepcionista", esRecepcionista);
        model.addAttribute("navEsTecnico", esTecnico);
        model.addAttribute("navPuedeGestionarUsuarios", esAdministrador);
        model.addAttribute("navPuedeGestionarClientes", puedeGestionarClientes);
        model.addAttribute("navPuedeGestionarEquipos", puedeGestionarEquipos);
        model.addAttribute("navPuedeGestionarTecnicos", esAdministrador);
        model.addAttribute("navPuedeGestionarServicios", esAdministrador);
        model.addAttribute("navPuedeGestionarInventario", esAdministrador);
        model.addAttribute("navPuedeGestionarCotizaciones", esAdministrador || esRecepcionista);
        model.addAttribute("navPuedeGestionarPagos", esAdministrador || esRecepcionista);
        model.addAttribute("navPuedeVerReportes", esAdministrador);
        model.addAttribute("navPuedeCrearOrdenes", puedeCrearOrdenes);
        model.addAttribute("navPuedeGestionarDiagnosticos", esAdministrador || esTecnico);
    }
}
