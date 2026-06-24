package com.nexoservice.inventario.repository;

import com.nexoservice.inventario.entity.ProductoInventario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductoInventarioRepository extends JpaRepository<ProductoInventario, Long> {

    boolean existsByCodigoIgnoreCase(String codigo);

    boolean existsByCodigoIgnoreCaseAndIdNot(String codigo, Long id);

    @Query("""
            select count(p)
            from ProductoInventario p
            where p.activo = true
              and p.stockActual <= p.stockMinimo
            """)
    long countProductosConStockBajo();

    Page<ProductoInventario> findByActivoTrue(Pageable pageable);

    Page<ProductoInventario> findByCodigoContainingIgnoreCaseOrNombreContainingIgnoreCaseOrCategoriaContainingIgnoreCase(
            String codigo,
            String nombre,
            String categoria,
            Pageable pageable
    );
}
