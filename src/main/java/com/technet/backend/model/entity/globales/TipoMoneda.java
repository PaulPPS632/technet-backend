package com.technet.backend.model.entity.globales;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TipoMoneda")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TipoMoneda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

}
