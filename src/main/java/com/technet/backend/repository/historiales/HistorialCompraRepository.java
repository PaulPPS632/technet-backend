package com.technet.backend.repository.historiales;

import com.technet.backend.model.entity.historiales.HistorialCompra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistorialCompraRepository extends JpaRepository<HistorialCompra, String> {
}
