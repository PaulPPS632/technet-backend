package com.technet.backend.model.dto.inventario;

import com.technet.backend.model.dto.users.UserResponse;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PedidosReStockResponse(
        String id,
        UserResponse usuario,
        LocalDateTime fecha,
        ProductoResponse producto,
        String estado,
        Long cantidad,
        String nota,
        String tenantId
) {
}


