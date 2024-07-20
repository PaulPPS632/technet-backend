package com.technet.backend.model.dto.inventario;

import java.util.List;

public record ProductoResponse(
    String id,
    String nombre, 
    String pn,
    String descripcion,
    Double stock,
    Double precio,
    String marca,
    String categoriamarca,
    String categoria,
    String subcategoria,
    Double garantia_cliente,
    Double garantia_total,
    String imagen_principal,
    List<String> imageurl
    ) {
    
}
