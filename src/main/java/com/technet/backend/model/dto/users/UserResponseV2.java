package com.technet.backend.model.dto.users;

import lombok.Builder;

@Builder
public record UserResponseV2 (
    String id,
    boolean regist,
    String email,
    String username,
    RolResponse rol
){}
