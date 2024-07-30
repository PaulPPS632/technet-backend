package com.technet.backend.model.entity.inventario;

import com.technet.backend.model.entity.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "PedidosReStock")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pedidos {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;


    private LocalDateTime fecha;
    @Column(length = 9000)
    private String productos;

    @Column(length = 5000)
    private String datospago;

    private String estado;
}
