package com.technet.backend.repository.inventario;

import com.technet.backend.model.entity.inventario.PedidosReStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidosReStockRepository extends JpaRepository<PedidosReStock, String> {
}
