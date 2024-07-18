package com.technet.backend.model.dto.documentos;

import com.technet.backend.model.dto.users.UserResponse;
import com.technet.backend.model.entity.globales.Entidad;
import com.technet.backend.model.entity.globales.TipoCondicion;
import com.technet.backend.model.entity.globales.TipoMoneda;
import com.technet.backend.model.entity.globales.TipoPago;
import com.technet.backend.model.entity.users.User;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record VentaResponse(
        UUID id,
        CorrelativoResponse correlativo,
        Entidad cliente,
        UserResponse usuario,
        TipoCondicion tipocondicion,
        TipoPago tipopago,
        TipoMoneda tipomoneda,
        Double tipo_cambio,
        LocalDateTime fecha_emision,
        LocalDateTime fecha_vencimiento,
        String nota,
        Double gravada,
        Double impuesto,
        Double total,
        LocalDateTime fechapago,
        String formapago
) {
}
