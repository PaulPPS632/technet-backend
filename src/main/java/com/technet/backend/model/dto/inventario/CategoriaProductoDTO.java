package com.technet.backend.model.dto.inventario;

import com.technet.backend.model.entity.globales.Archivo;
import com.technet.backend.model.entity.inventario.Producto;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class CategoriaProductoDTO {
    private String categoria;
    private ProductoResponse producto;
    public CategoriaProductoDTO(String categoria, Producto producto) {
        this.categoria = categoria;
        this.producto = mapToProductoResponse(producto);
    }
    private ProductoResponse mapToProductoResponse(Producto producto){
        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getPn(),
                producto.getDescripcion(),
                producto.getStock(),
                producto.getPrecio(),
                producto.getCategoriamarca().getMarca().getNombre(),
                producto.getCategoriamarca().getNombre(),
                producto.getSubcategoria().getCategoria().getNombre(),
                producto.getSubcategoria().getNombre(),
                producto.getGarantia_cliente(),
                producto.getGarantia_total(),
                producto.getArchivo_Principal() != null ? producto.getArchivo_Principal().getUrl() : "",
                producto.getArchivos().stream().map(Archivo::getUrl).collect(Collectors.toList())
        );
    }
}
