package com.technet.backend.repository.inventario;
import com.technet.backend.model.entity.inventario.Producto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, String>, JpaSpecificationExecutor<Producto> {


    @Query("SELECT p FROM Producto p WHERE p.nombre LIKE %:keyword% OR p.descripcion LIKE %:keyword%")
    List<Producto> findByNombreOrDescripcionContaining(@Param("keyword") String keyword);
    /*
    Page<Producto> findByCategoriamarca_Marca_NombreInAndSubcategoria_Categoria_NombreIn(List<String> marca, List<String> categoria, Pageable pageable);

    Page<Producto> findByCategoriamarca_Marca_NombreIn(List<String> marca, Pageable pageable);
    Page<Producto> findBySubcategoria_Categoria_NombreIn(List<String> categoria, Pageable pageable);

    Page<Producto> findByCategoriamarca_Marca_NombreAndSubcategoria_Nombre(String marca, String subcategoria, Pageable pageable);


    //Page<Producto> findByCategoriamarca_Marca_NombreInAndSubcategoria_Categoria_NombreInOrSubcategoria_NombreInAndNombreContaining(List<String> marca, List<String> categoria, List<String> subcategoria,String nombre, Pageable pageable);

    // Consulta para buscar por marca y nombre
    Page<Producto> findByCategoriamarca_Marca_NombreInAndNombreContaining(
            List<String> marca,
            String nombre,
            Pageable pageable);

    // Consulta para buscar por categoría, subcategoría y nombre
    Page<Producto> findBySubcategoria_Categoria_NombreInOrSubcategoria_NombreInAndNombreContaining(
            List<String> categoria,
            List<String> subcategoria,
            String nombre,
            Pageable pageable);

     */
}
