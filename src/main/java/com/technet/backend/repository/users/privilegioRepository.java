package com.technet.backend.repository.users;

import com.technet.backend.model.entity.users.Privilegio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface privilegioRepository extends JpaRepository<Privilegio, Long> {
}
