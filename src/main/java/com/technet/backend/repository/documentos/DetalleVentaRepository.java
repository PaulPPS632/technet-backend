package com.technet.backend.repository.documentos;

import com.technet.backend.model.entity.documentos.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
}
