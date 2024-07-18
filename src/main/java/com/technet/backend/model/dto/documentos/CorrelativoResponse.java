package com.technet.backend.model.dto.documentos;

import lombok.Builder;

@Builder
public record CorrelativoResponse(
        String prefijo,
        Long numeracion,
        Long correlativo,
        String documento
) {
}
