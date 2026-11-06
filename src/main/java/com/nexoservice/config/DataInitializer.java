package com.nexoservice.config;

import com.nexoservice.rol.entity.NombreRol;
import com.nexoservice.rol.entity.Rol;
import com.nexoservice.rol.repository.RolRepository;
import com.nexoservice.tecnico.entity.DisponibilidadTecnico;
import com.nexoservice.tecnico.entity.Tecnico;
import com.nexoservice.tecnico.repository.TecnicoRepository;
import com.nexoservice.tipoactivo.entity.TipoActivo;
import com.nexoservice.tipoactivo.repository.TipoActivoRepository;
import com.nexoservice.usuario.entity.Usuario;
import com.nexoservice.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final TecnicoRepository tecnicoRepository;
    private final TipoActivoRepository tipoActivoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    @Value("${app.demo-users.enabled:false}")
    private boolean demoUsersEnabled;

    @Override
    public void run(String... args) {
        migrarEstadosOrdenAntiguos();

        Rol admin = crearRolSiNoExiste(NombreRol.ADMINISTRADOR);
        Rol recepcionista = crearRolSiNoExiste(NombreRol.RECEPCIONISTA);
        Rol tecnico = crearRolSiNoExiste(NombreRol.TECNICO);
        crearRolSiNoExiste(NombreRol.CLIENTE);

        // Only seed predictable demonstration credentials when explicitly enabled.
        // Never enable this option in a public or production deployment.
        if (demoUsersEnabled) {
            Tecnico tecnicoDemo = crearTecnicoDemoSiNoExiste();
            crearOActualizarUsuario("admin", "admin@nexoservice.com", "admin123", admin, null);
            crearOActualizarUsuario("recepcion", "recepcion@nexoservice.com", "recep123", recepcionista, null);
            crearOActualizarUsuario("tecnico", "tecnico@nexoservice.com", "tec123", tecnico, tecnicoDemo);
        }

        crearTipoActivoSiNoExiste("Aire acondicionado", "Equipos de climatizacion residencial, comercial o industrial");
        crearTipoActivoSiNoExiste("Vehiculo", "Autos, motos u otros activos automotrices");
        crearTipoActivoSiNoExiste("Equipo medico", "Equipos utilizados en atencion, diagnostico o soporte medico");
        crearTipoActivoSiNoExiste("Instrumento de calibracion", "Instrumentos sujetos a calibracion, inspeccion o certificacion");
        crearTipoActivoSiNoExiste("Equipo de computo", "Computadoras, laptops, impresoras y perifericos");
    }

    private Rol crearRolSiNoExiste(NombreRol nombreRol) {
        return rolRepository.findByNombre(nombreRol)
                .orElseGet(() -> rolRepository.save(Rol.builder().nombre(nombreRol).build()));
    }

    private void crearOActualizarUsuario(String username, String correo, String rawPassword, Rol rol, Tecnico tecnico) {
        Usuario usuarioExistente = usuarioRepository.findByUsername(username).orElse(null);

        if (usuarioExistente != null) {
            usuarioExistente.setRol(rol);
            usuarioExistente.setTecnico(tecnico);
            usuarioExistente.setActivo(true);
            usuarioRepository.save(usuarioExistente);
            return;
        }

        Usuario usuario = Usuario.builder()
                .username(username)
                .correo(correo)
                .password(passwordEncoder.encode(rawPassword))
                .rol(rol)
                .tecnico(tecnico)
                .activo(true)
                .build();

        usuarioRepository.save(usuario);
    }

    private Tecnico crearTecnicoDemoSiNoExiste() {
        return tecnicoRepository.findByCorreo("tecnico@nexoservice.com")
                .orElseGet(() -> tecnicoRepository.save(Tecnico.builder()
                        .nombres("Tecnico")
                        .apellidos("Demo")
                        .especialidad("Diagnostico y reparacion")
                        .correo("tecnico@nexoservice.com")
                        .telefono("955777888")
                        .disponibilidad(DisponibilidadTecnico.DISPONIBLE)
                        .activo(true)
                        .build()));
    }

    private void crearTipoActivoSiNoExiste(String nombre, String descripcion) {
        if (tipoActivoRepository.existsByNombreIgnoreCase(nombre)) {
            return;
        }

        tipoActivoRepository.save(TipoActivo.builder()
                .nombre(nombre)
                .descripcion(descripcion)
                .activo(true)
                .build());
    }

    private void migrarEstadosOrdenAntiguos() {
        actualizarEstado("ordenes_servicio", "estado", "DIAGNOSTICADA", "ESPERANDO_APROBACION");
        actualizarEstado("ordenes_servicio", "estado", "EN_PROCESO", "EN_EJECUCION");
        actualizarEstado("ordenes_servicio", "estado", "EN_ESPERA", "ESPERANDO_REPUESTO");
        actualizarEstado("historial_orden", "estado_anterior", "DIAGNOSTICADA", "ESPERANDO_APROBACION");
        actualizarEstado("historial_orden", "estado_anterior", "EN_PROCESO", "EN_EJECUCION");
        actualizarEstado("historial_orden", "estado_anterior", "EN_ESPERA", "ESPERANDO_REPUESTO");
        actualizarEstado("historial_orden", "estado_nuevo", "DIAGNOSTICADA", "ESPERANDO_APROBACION");
        actualizarEstado("historial_orden", "estado_nuevo", "EN_PROCESO", "EN_EJECUCION");
        actualizarEstado("historial_orden", "estado_nuevo", "EN_ESPERA", "ESPERANDO_REPUESTO");
    }

    private void actualizarEstado(String tabla, String columna, String anterior, String nuevo) {
        jdbcTemplate.update("UPDATE " + tabla + " SET " + columna + " = ? WHERE " + columna + " = ?", nuevo, anterior);
    }
}
