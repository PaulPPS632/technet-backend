package com.technet.backend.model.dto.documentos;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record RegistrarCompraRequest(
        String documento,
        String documento_cliente,
        String usuario_id,
        Long id_tipocondicion,
        Long id_tipopago,
        Long id_tipomoneda,
        Double tipo_cambio,
        LocalDateTime fecha_emision,
        LocalDateTime fecha_vencimiento,
        String nota,
        Double gravada,
        Double impuesto,
        Double total,
        LocalDateTime fechapago,
        String formapago,
        List<DetalleCompraRequest> detalles
) {
}
