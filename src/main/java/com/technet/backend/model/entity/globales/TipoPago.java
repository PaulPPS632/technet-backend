package com.technet.backend.model.entity.globales;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TipoPago")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TipoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;


}
