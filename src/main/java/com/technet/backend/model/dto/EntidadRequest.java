package com.technet.backend.model.dto;

public record EntidadRequest(
    String nombre,
    String documento,
    String direccion,
    String telefono,
    String email,
    Long id_tipoEntidad
) {
}
