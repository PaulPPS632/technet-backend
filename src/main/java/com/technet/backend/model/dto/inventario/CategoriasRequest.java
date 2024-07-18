package com.technet.backend.model.dto.inventario;

import java.util.List;

public record CategoriasRequest(
        List<CategoriaRequest> categorias
) {
}
