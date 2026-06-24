package com.nexoservice.security;

import com.nexoservice.usuario.entity.Usuario;
import com.nexoservice.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No se encontro el usuario " + username));

        return new User(
                usuario.getUsername(),
                usuario.getPassword(),
                Boolean.TRUE.equals(usuario.getActivo()),
                true,
                true,
                true,
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombre().name()))
        );
    }
}
