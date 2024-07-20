package com.technet.backend.model.entity.inventario;

import java.util.List;

import com.technet.backend.model.entity.globales.Archivo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Producto")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String nombre;
    private String pn;

    @Column(length = 2000)
    private String descripcion;

    private Double garantia_cliente;
    private Double garantia_total;
    private Double Stock;
    private Double precio;
    @ManyToOne
    @JoinColumn(name = "id_archivo")
    private Archivo archivo_Principal;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "producto_archivo",
            joinColumns = @JoinColumn(name = "producto_id"),
            inverseJoinColumns = @JoinColumn(name = "archivo_id")
    )
    private List<Archivo> archivos;

    @ManyToOne
    @JoinColumn(name = "id_categoriamarca")
    private CategoriaMarca categoriamarca;

    @ManyToOne
    @JoinColumn(name = "id_subcategoria")
    private SubCategoria subcategoria;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private List<Lote> lote;

}
