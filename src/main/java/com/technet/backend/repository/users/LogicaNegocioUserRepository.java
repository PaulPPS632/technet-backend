package com.technet.backend.repository.users;

import com.technet.backend.model.entity.users.LogicaNegocioUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LogicaNegocioUserRepository extends JpaRepository<LogicaNegocioUser, String> {
    Optional<LogicaNegocioUser> findByUsuario(String usuario_id);
}
