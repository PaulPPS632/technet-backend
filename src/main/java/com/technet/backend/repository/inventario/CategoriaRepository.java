package com.technet.backend.repository.inventario;

import com.technet.backend.model.entity.inventario.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

    
} 