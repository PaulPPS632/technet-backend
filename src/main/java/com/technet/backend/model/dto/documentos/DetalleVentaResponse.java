package com.technet.backend.model.dto.documentos;

import lombok.Builder;

import java.util.List;

@Builder
public record DetalleVentaResponse(
        String sn,
        Double precio
) {
}
