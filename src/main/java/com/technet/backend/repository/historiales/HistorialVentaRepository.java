package com.technet.backend.repository.historiales;

import com.technet.backend.model.entity.historiales.HistorialVenta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistorialVentaRepository extends JpaRepository<HistorialVenta, String> {
}
