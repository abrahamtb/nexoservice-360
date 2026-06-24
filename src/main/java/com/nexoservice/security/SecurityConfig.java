package com.nexoservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**").permitAll()
                        .requestMatchers("/usuarios/**", "/reportes/**", "/servicios/**", "/tipos-activo/**")
                        .hasAnyRole("ADMINISTRADOR", "ADMIN")
                        .requestMatchers("/tecnicos/**")
                        .hasAnyRole("ADMINISTRADOR", "ADMIN")
                        .requestMatchers("/inventario/**")
                        .hasAnyRole("ADMINISTRADOR", "ADMIN")
                        .requestMatchers("/clientes/**", "/equipos/**")
                        .hasAnyRole("ADMINISTRADOR", "ADMIN", "RECEPCIONISTA")
                        .requestMatchers(HttpMethod.GET, "/cotizaciones/**")
                        .hasAnyRole("ADMINISTRADOR", "ADMIN", "RECEPCIONISTA", "TECNICO")
                        .requestMatchers("/cotizaciones/**", "/pagos/**")
                        .hasAnyRole("ADMINISTRADOR", "ADMIN", "RECEPCIONISTA")
                        .requestMatchers(HttpMethod.GET, "/ordenes/nueva")
                        .hasAnyRole("ADMINISTRADOR", "ADMIN", "RECEPCIONISTA")
                        .requestMatchers(HttpMethod.GET, "/ordenes", "/ordenes/*", "/consultas/**")
                        .hasAnyRole("ADMINISTRADOR", "ADMIN", "RECEPCIONISTA", "TECNICO")
                        .requestMatchers(HttpMethod.POST, "/ordenes/*/cambiar-estado")
                        .hasAnyRole("ADMINISTRADOR", "ADMIN", "RECEPCIONISTA", "TECNICO")
                        .requestMatchers("/ordenes/**")
                        .hasAnyRole("ADMINISTRADOR", "ADMIN", "RECEPCIONISTA")
                        .requestMatchers("/diagnosticos/**", "/trabajos-orden/**")
                        .hasAnyRole("ADMINISTRADOR", "ADMIN", "TECNICO")
                        .requestMatchers(HttpMethod.GET, "/api/ordenes/**")
                        .hasAnyRole("ADMINISTRADOR", "ADMIN", "RECEPCIONISTA", "TECNICO")
                        .requestMatchers(HttpMethod.PATCH, "/api/ordenes/*/cambiar-estado")
                        .hasAnyRole("ADMINISTRADOR", "ADMIN", "RECEPCIONISTA", "TECNICO")
                        .requestMatchers("/api/ordenes/**")
                        .hasAnyRole("ADMINISTRADOR", "ADMIN", "RECEPCIONISTA")
                        .requestMatchers("/api/pagos/**")
                        .hasAnyRole("ADMINISTRADOR", "ADMIN", "RECEPCIONISTA")
                        .requestMatchers(HttpMethod.GET, "/api/cotizaciones/**")
                        .hasAnyRole("ADMINISTRADOR", "ADMIN", "RECEPCIONISTA", "TECNICO")
                        .requestMatchers("/api/cotizaciones/**")
                        .hasAnyRole("ADMINISTRADOR", "ADMIN", "RECEPCIONISTA")
                        .requestMatchers("/api/diagnosticos/**", "/api/trabajos-orden/**")
                        .hasAnyRole("ADMINISTRADOR", "ADMIN", "TECNICO")
                        .requestMatchers("/api/evidencias/**")
                        .hasAnyRole("ADMINISTRADOR", "ADMIN", "TECNICO")
                        .requestMatchers("/api/clientes/**", "/api/equipos/**")
                        .hasAnyRole("ADMINISTRADOR", "ADMIN", "RECEPCIONISTA")
                        .requestMatchers("/api/tecnicos/**", "/api/servicios/**", "/api/tipos-activo/**")
                        .hasAnyRole("ADMINISTRADOR", "ADMIN")
                        .requestMatchers("/api/mantenimientos-programados/**")
                        .hasAnyRole("ADMINISTRADOR", "ADMIN", "RECEPCIONISTA")
                        .requestMatchers("/api/inventario/**")
                        .hasAnyRole("ADMINISTRADOR", "ADMIN")
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/access-denied")
                )
                .httpBasic(Customizer.withDefaults())
                .authenticationProvider(authenticationProvider())
                .build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
