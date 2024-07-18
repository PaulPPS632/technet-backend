package com.technet.backend.repository.inventario;
import com.technet.backend.model.entity.inventario.Lote;
import com.technet.backend.model.entity.inventario.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoteRepository extends JpaRepository<Lote, Long>{
    Long countByProducto(Producto producto);
}
