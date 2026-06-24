package com.nexoservice.web.controller;

import com.nexoservice.exception.BusinessException;
import com.nexoservice.rol.entity.NombreRol;
import com.nexoservice.usuario.entity.Usuario;
import com.nexoservice.usuario.service.UsuarioService;
import com.nexoservice.web.form.UsuarioForm;
import com.nexoservice.web.service.WebCatalogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioWebController {

    private static final List<NombreRol> ROLES_ASIGNABLES = List.of(
            NombreRol.ADMINISTRADOR,
            NombreRol.RECEPCIONISTA,
            NombreRol.TECNICO,
            NombreRol.CLIENTE
    );

    private final UsuarioService usuarioService;
    private final WebCatalogService webCatalogService;

    @GetMapping
    public String listar(Model model) {
        List<Usuario> usuarios = usuarioService.listar();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("resumenUsuarios", obtenerResumenUsuarios(usuarios));
        model.addAttribute("usuariosActivos", usuarios.stream().filter(usuario -> Boolean.TRUE.equals(usuario.getActivo())).count());
        return "usuarios/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        cargarFormulario(model, new UsuarioForm(), null);
        return "usuarios/form";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("usuario") UsuarioForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            cargarFormulario(model, form, null);
            return "usuarios/form";
        }

        try {
            usuarioService.crear(form);
            return "redirect:/usuarios";
        } catch (BusinessException exception) {
            model.addAttribute("mensajeError", exception.getMessage());
            cargarFormulario(model, form, null);
            return "usuarios/form";
        }
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.buscarEntidadPorId(id);
        cargarFormulario(model, toForm(usuario), id);
        return "usuarios/form";
    }

    @PostMapping("/{id}")
    public String actualizar(
            @PathVariable Long id,
            @Valid @ModelAttribute("usuario") UsuarioForm form,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            cargarFormulario(model, form, id);
            return "usuarios/form";
        }

        try {
            usuarioService.actualizar(id, form);
            return "redirect:/usuarios";
        } catch (BusinessException exception) {
            model.addAttribute("mensajeError", exception.getMessage());
            cargarFormulario(model, form, id);
            return "usuarios/form";
        }
    }

    private void cargarFormulario(Model model, UsuarioForm form, Long usuarioId) {
        model.addAttribute("usuario", form);
        model.addAttribute("usuarioId", usuarioId);
        model.addAttribute("roles", ROLES_ASIGNABLES);
        model.addAttribute("tecnicos", webCatalogService.listarTecnicos());
    }

    private UsuarioForm toForm(Usuario usuario) {
        UsuarioForm form = new UsuarioForm();
        form.setUsername(usuario.getUsername());
        form.setCorreo(usuario.getCorreo());
        form.setRol(usuario.getRol().getNombre() == NombreRol.ADMIN ? NombreRol.ADMINISTRADOR : usuario.getRol().getNombre());
        form.setTecnicoId(usuario.getTecnico() == null ? null : usuario.getTecnico().getId());
        form.setActivo(usuario.getActivo());
        return form;
    }

    private Map<String, Long> obtenerResumenUsuarios(List<Usuario> usuarios) {
        Map<String, Long> resumen = new LinkedHashMap<>();

        for (NombreRol rol : ROLES_ASIGNABLES) {
            resumen.put(nombreVisible(rol), usuarios.stream()
                    .filter(usuario -> normalizarRol(usuario.getRol().getNombre()) == rol)
                    .count());
        }

        return resumen;
    }

    private NombreRol normalizarRol(NombreRol rol) {
        return rol == NombreRol.ADMIN ? NombreRol.ADMINISTRADOR : rol;
    }

    private String nombreVisible(NombreRol rol) {
        return switch (rol) {
            case ADMINISTRADOR -> "Administradores";
            case RECEPCIONISTA -> "Recepcionistas";
            case TECNICO -> "Tecnicos";
            case CLIENTE -> "Clientes futuros";
            case ADMIN -> "Administradores";
        };
    }
}
