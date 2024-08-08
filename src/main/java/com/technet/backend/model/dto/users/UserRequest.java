package com.technet.backend.model.dto.users;

import lombok.Builder;

@Builder
public record UserRequest(
        String id,
        boolean regist,
        String email,
        String username,
        String rol
) {
}
