package com.technet.backend.repository.globales;

import com.technet.backend.model.entity.globales.Entidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EntidadRepository extends JpaRepository<Entidad, String> {
    @Query("SELECT e FROM Entidad e WHERE e.documento LIKE %:keyword%")
    List<Entidad> findByDocumento(@Param("keyword") String keyword);

}
