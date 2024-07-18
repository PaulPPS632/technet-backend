package com.technet.backend.service.inventario;

import com.technet.backend.model.dto.inventario.ProductoResponse;
import com.technet.backend.model.dto.inventario.ProductoSerieResponse;
import com.technet.backend.model.dto.inventario.ProductoSeriesRequest;
import com.technet.backend.model.entity.inventario.Lote;
import com.technet.backend.model.entity.inventario.Producto;

import com.technet.backend.model.entity.inventario.ProductoSerie;
import com.technet.backend.repository.inventario.LoteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import com.technet.backend.repository.inventario.ProductoRepository;
import com.technet.backend.repository.inventario.ProductoSerieRepository;

import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoSerieService {

    private final ProductoSerieRepository productoSerieRepository;
    private final LoteRepository loteRepository;
    private final ProductoRepository productoRepository;

    public void save(ProductoSeriesRequest productoSeriesRequest){
        Optional<Lote> optionallote = loteRepository.findById(productoSeriesRequest.id_Lote());
        Optional<Producto> optionalProducto = productoRepository.findById(productoSeriesRequest.id_producto());
        if(optionallote.isEmpty() && optionalProducto.isEmpty())throw new EntityNotFoundException("Se necesita almenos 1 id de referencia (Producto o Lote)");

        Producto producto = optionalProducto.get();
        Lote lote = optionallote.orElseGet(() -> {
            List<Lote> lotes = producto.getLote();
            if (lotes.isEmpty()) {
                throw new EntityNotFoundException("No hay lotes disponibles para el producto");
            }
            return lotes.get(lotes.size() - 1);  // Obtiene el último elemento de la lista de lotes
        });

        if(lote.getProductoserie().isEmpty()) lote.setProductoserie(new ArrayList<>());

        List<ProductoSerie> nuevasSeries = Arrays.stream(productoSeriesRequest.sn()).map(
                request -> ProductoSerie.builder()
                        .sn(request.toString())
                        .lote(lote)
                        .build()
        ).collect(Collectors.toList());

        productoSerieRepository.saveAll(nuevasSeries);

        lote.getProductoserie().addAll(nuevasSeries);
        loteRepository.save(lote);
    }

    public List<ProductoSerieResponse> getAllStock(String id){
        Optional<Producto> product = productoRepository.findById(id);
        if(product.isPresent()){
            return product.get().getLote().stream().flatMap(res -> res.getProductoserie().stream()).filter(serie -> serie.getEstadoproducto().getId() == 1).map(this::mapToProductoSerieResponse).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
    public List<ProductoSerieResponse> getAll(){
        return productoSerieRepository.findAll().stream().map(this::mapToProductoSerieResponse).collect(Collectors.toList());
    }
    public List<ProductoSerieResponse> getAll(String id){
        Optional<Producto> product = productoRepository.findById(id);
        if(product.isPresent()){
            return product.get().getLote().stream().flatMap(res -> res.getProductoserie().stream()).map(this::mapToProductoSerieResponse).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
    public ProductoResponse getProductbelong(String sn){
        Optional<ProductoSerie> serie = productoSerieRepository.findBySn(sn);
        if(serie.isEmpty()) throw new EntityNotFoundException("No se Encontro la serie: " + sn);
        if(serie.get().getEstadoproducto().getId() == 1){
            Producto producto = serie.get().getLote().getProducto();
            return mapToProductoResponse(producto);
        }
        return null;
    }
    public ProductoResponse mapToProductoResponse(Producto producto){
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
                producto.getImagenuri()
        );
    }
    public ProductoSerieResponse mapToProductoSerieResponse(ProductoSerie productoSerie){
        return new ProductoSerieResponse(productoSerie.getSn());
    }

}
