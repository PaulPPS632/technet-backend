package com.technet.backend.repository.globales;

import com.technet.backend.model.entity.globales.Archivo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchivoRepository extends JpaRepository<Archivo,String> {
}
