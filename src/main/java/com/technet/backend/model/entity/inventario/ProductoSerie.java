package com.technet.backend.model.entity.inventario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "productoserie")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductoSerie {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String sn;
    

    @ManyToOne
    @JoinColumn(name = "id_lote")
    private Lote lote;

    @ManyToOne
    @JoinColumn(name = "id_estadoproducto")
    private EstadoProducto estadoproducto;


}

