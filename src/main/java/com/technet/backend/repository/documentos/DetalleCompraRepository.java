package com.technet.backend.repository.documentos;

import com.technet.backend.model.entity.documentos.DetalleCompra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, String> {
}
