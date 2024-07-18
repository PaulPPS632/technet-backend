package com.technet.backend.repository.correlativos;

import com.technet.backend.model.entity.correlativos.TipoComprobante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoComprobanteRepository extends JpaRepository<TipoComprobante, Long> {
    Optional<TipoComprobante> findByPrefijo(String prefijo);
}
