package com.technet.backend.model.entity.globales;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="TipoEntidad")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoEntidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;
    private int cantdigitos;


}
