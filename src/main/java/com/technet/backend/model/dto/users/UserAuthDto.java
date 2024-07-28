package com.technet.backend.model.dto.users;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public record UserAuthDto(
        String id,
        String username,
        String email,
        String password,
        boolean regist,
        String rol
) {
}
