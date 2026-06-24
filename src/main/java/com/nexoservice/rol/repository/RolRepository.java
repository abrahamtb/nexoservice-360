package com.nexoservice.rol.repository;

import com.nexoservice.rol.entity.NombreRol;
import com.nexoservice.rol.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {

    Optional<Rol> findByNombre(NombreRol nombre);
}
