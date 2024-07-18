package com.technet.backend.repository.documentos;

import com.technet.backend.model.entity.documentos.Compra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CompraRepository extends JpaRepository<Compra, UUID> {
}
