package com.technet.backend.model.dto.inventario;

public record SubCategoriaResponse(
        Long id,
        String nombre,
        String descripcion,
        String categoria
) {
}
