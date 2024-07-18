package com.technet.backend.repository.documentos;

import com.technet.backend.model.entity.documentos.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {

}
