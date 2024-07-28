package com.technet.backend.model.dto.auth;

import lombok.Builder;

@Builder
public record AuthResponse(
        String token
) {
}
