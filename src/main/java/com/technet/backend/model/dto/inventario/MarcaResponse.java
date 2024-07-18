package com.technet.backend.model.dto.inventario;

import java.util.List;

public record MarcaResponse(
        Long id,
        String nombre,
        List<CategoriaMarcaResponse> categorias) {
    
}
