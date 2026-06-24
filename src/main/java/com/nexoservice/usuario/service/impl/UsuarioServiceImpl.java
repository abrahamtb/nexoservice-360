package com.nexoservice.usuario.service.impl;

import com.nexoservice.exception.BusinessException;
import com.nexoservice.exception.DuplicateResourceException;
import com.nexoservice.exception.ResourceNotFoundException;
import com.nexoservice.rol.entity.NombreRol;
import com.nexoservice.rol.entity.Rol;
import com.nexoservice.rol.repository.RolRepository;
import com.nexoservice.tecnico.entity.Tecnico;
import com.nexoservice.tecnico.repository.TecnicoRepository;
import com.nexoservice.usuario.entity.Usuario;
import com.nexoservice.usuario.repository.UsuarioRepository;
import com.nexoservice.usuario.service.UsuarioService;
import com.nexoservice.web.form.UsuarioForm;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final TecnicoRepository tecnicoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return usuarioRepository.findAll(Sort.by("username").ascending());
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario buscarEntidadPorId(Long id) {
        return obtenerUsuario(id);
    }

    @Override
    @Transactional
    public Usuario crear(UsuarioForm form) {
        validarPasswordCreacion(form);
        validarDuplicadosCrear(form);

        Rol rol = obtenerRol(form.getRol());
        Tecnico tecnico = obtenerTecnicoSiCorresponde(form);

        Usuario usuario = Usuario.builder()
                .username(form.getUsername().trim())
                .correo(form.getCorreo().trim())
                .password(passwordEncoder.encode(form.getPassword()))
                .rol(rol)
                .tecnico(tecnico)
                .activo(Boolean.TRUE.equals(form.getActivo()))
                .build();

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario actualizar(Long id, UsuarioForm form) {
        Usuario usuario = obtenerUsuario(id);
        validarDuplicadosActualizar(id, form);

        usuario.setUsername(form.getUsername().trim());
        usuario.setCorreo(form.getCorreo().trim());
        usuario.setRol(obtenerRol(form.getRol()));
        usuario.setTecnico(obtenerTecnicoSiCorresponde(form));
        usuario.setActivo(Boolean.TRUE.equals(form.getActivo()));

        if (StringUtils.hasText(form.getPassword())) {
            usuario.setPassword(passwordEncoder.encode(form.getPassword()));
        }

        return usuario;
    }

    private Usuario obtenerUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el usuario con id " + id));
    }

    private Rol obtenerRol(NombreRol nombreRol) {
        return rolRepository.findByNombre(nombreRol)
                .orElseGet(() -> rolRepository.save(Rol.builder().nombre(nombreRol).build()));
    }

    private Tecnico obtenerTecnicoSiCorresponde(UsuarioForm form) {
        if (form.getRol() != NombreRol.TECNICO) {
            return null;
        }

        if (form.getTecnicoId() == null) {
            throw new BusinessException("Un usuario TECNICO debe estar vinculado a un tecnico");
        }

        return tecnicoRepository.findById(form.getTecnicoId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el tecnico con id " + form.getTecnicoId()));
    }

    private void validarPasswordCreacion(UsuarioForm form) {
        if (!StringUtils.hasText(form.getPassword())) {
            throw new BusinessException("La contrasena es obligatoria al crear un usuario");
        }
    }

    private void validarDuplicadosCrear(UsuarioForm form) {
        if (usuarioRepository.existsByUsername(form.getUsername().trim())) {
            throw new DuplicateResourceException("Ya existe un usuario con username " + form.getUsername());
        }

        if (usuarioRepository.existsByCorreo(form.getCorreo().trim())) {
            throw new DuplicateResourceException("Ya existe un usuario con correo " + form.getCorreo());
        }
    }

    private void validarDuplicadosActualizar(Long id, UsuarioForm form) {
        if (usuarioRepository.existsByUsernameAndIdNot(form.getUsername().trim(), id)) {
            throw new DuplicateResourceException("Ya existe otro usuario con username " + form.getUsername());
        }

        if (usuarioRepository.existsByCorreoAndIdNot(form.getCorreo().trim(), id)) {
            throw new DuplicateResourceException("Ya existe otro usuario con correo " + form.getCorreo());
        }
    }
}
