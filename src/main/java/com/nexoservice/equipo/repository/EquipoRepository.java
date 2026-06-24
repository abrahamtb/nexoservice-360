package com.nexoservice.equipo.repository;

import com.nexoservice.equipo.entity.Equipo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EquipoRepository extends JpaRepository<Equipo, Long> {

    boolean existsByNumeroSerie(String numeroSerie);

    boolean existsByNumeroSerieAndIdNot(String numeroSerie, Long id);

    Page<Equipo> findByClienteId(Long clienteId, Pageable pageable);

    @Query("""
            select e from Equipo e
            where lower(e.tipo) like lower(concat('%', :buscar, '%'))
               or lower(e.marca) like lower(concat('%', :buscar, '%'))
               or lower(e.modelo) like lower(concat('%', :buscar, '%'))
               or lower(e.numeroSerie) like lower(concat('%', :buscar, '%'))
               or lower(e.codigoInterno) like lower(concat('%', :buscar, '%'))
               or lower(e.ubicacion) like lower(concat('%', :buscar, '%'))
            """)
    Page<Equipo> buscar(@Param("buscar") String buscar, Pageable pageable);
}
