package com.technet.backend.model.entity.documentos;

import com.technet.backend.model.entity.inventario.ProductoSerie;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "DetalleVenta")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "id_venta")
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "id_productoserie")
    private ProductoSerie productoserie;

    private Double precio_neto;

}
