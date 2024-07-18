package com.technet.backend.repository.inventario;

import com.technet.backend.model.entity.inventario.ProductoSerie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductoSerieRepository extends JpaRepository<ProductoSerie, String> {
    Optional<ProductoSerie> findBySn(String sn);
}
