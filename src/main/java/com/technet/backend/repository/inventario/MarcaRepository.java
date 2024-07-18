package com.technet.backend.repository.inventario;

import com.technet.backend.model.entity.inventario.Marca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarcaRepository extends JpaRepository<Marca, Long> {
    
}
