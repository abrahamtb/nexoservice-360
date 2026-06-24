package com.nexoservice.diagnostico.repository;

import com.nexoservice.diagnostico.entity.Diagnostico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Long> {

    boolean existsByOrdenServicioId(Long ordenServicioId);

    Optional<Diagnostico> findByOrdenServicioId(Long ordenServicioId);
}
