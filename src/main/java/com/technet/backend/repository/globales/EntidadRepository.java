package com.technet.backend.repository.globales;

import com.technet.backend.model.entity.globales.Entidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EntidadRepository extends JpaRepository<Entidad, String> {
    List<Entidad> findByDocumentoContaining(String keyword);

    Optional<Entidad> findByDocumento(String Documento);
}
