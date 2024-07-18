package com.technet.backend.repository.correlativos;

import com.technet.backend.model.entity.correlativos.Correlativo;
import com.technet.backend.model.entity.correlativos.NumeracionComprobante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CorrelativoRepository extends JpaRepository<Correlativo, UUID> {
    long countByNumeracioncomprobante(NumeracionComprobante numeracioncomprobante);
}
