package com.nexoservice.cliente.repository;

import com.nexoservice.cliente.entity.Cliente;
import com.nexoservice.cliente.entity.TipoDocumento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByTipoDocumentoAndNumeroDocumento(TipoDocumento tipoDocumento, String numeroDocumento);

    boolean existsByTipoDocumentoAndNumeroDocumentoAndIdNot(TipoDocumento tipoDocumento, String numeroDocumento, Long id);

    Page<Cliente> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCaseOrNumeroDocumentoContainingIgnoreCase(
            String nombres,
            String apellidos,
            String numeroDocumento,
            Pageable pageable
    );
}
