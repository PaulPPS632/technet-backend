package com.technet.backend.model.dto.inventario;

import java.time.LocalDateTime;

public record LoteResponse(Long id, String nombre, LocalDateTime fecha) {
}
