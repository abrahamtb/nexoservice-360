package com.nexoservice.usuario.service;

import com.nexoservice.usuario.entity.Usuario;
import com.nexoservice.web.form.UsuarioForm;

import java.util.List;

public interface UsuarioService {

    List<Usuario> listar();

    Usuario buscarEntidadPorId(Long id);

    Usuario crear(UsuarioForm form);

    Usuario actualizar(Long id, UsuarioForm form);
}
