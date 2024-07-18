package com.technet.backend.repository.users;

import com.technet.backend.model.entity.users.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Long> {
}
