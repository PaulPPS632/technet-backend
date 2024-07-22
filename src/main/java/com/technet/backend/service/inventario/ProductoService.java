package com.technet.backend.service.inventario;
import com.technet.backend.exception.ResourceNotFoundException;
import com.technet.backend.model.entity.globales.Archivo;
import com.technet.backend.model.entity.inventario.SubCategoria;
import com.technet.backend.repository.globales.ArchivoRepository;
import com.technet.backend.repository.inventario.CategoriaMarcaRepository;
import com.technet.backend.repository.inventario.ProductoRepository;
import com.technet.backend.model.dto.inventario.ProductoRequest;
import com.technet.backend.model.dto.inventario.ProductoResponse;
import com.technet.backend.model.entity.inventario.CategoriaMarca;
import com.technet.backend.model.entity.inventario.Producto;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.technet.backend.repository.inventario.ProductoSpecifications;
import com.technet.backend.repository.inventario.SubCategoriaRepository;
import com.technet.backend.service.globales.S3service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final CategoriaMarcaRepository categoriaMarcaRepository;
    private final SubCategoriaRepository subCategoriaRepository;
    private final S3service s3service;
    private final ArchivoRepository archivoRepository;

    @Value("${aws.namebucket}")
    private String namebucket;

    @Value("${aws.region}")
    private String region;

    public List<ProductoResponse> getAll(){
        List<Producto> productos = productoRepository.findAll();
        return productos.stream().map(this::mapToProductoResponse).toList();
    }

    public ProductoResponse getById(String id) {
        Optional<Producto> producto = productoRepository.findById(id);
        if(producto.isPresent()){
            return mapToProductoResponse(producto.get());
        }else {
            throw new ResourceNotFoundException("Producto no encontrado con id: " + id);
        }
    }

    public void save(ProductoRequest producto, List<MultipartFile> files, MultipartFile fileprincipal){
        Optional<CategoriaMarca> categoriamarcaoptional = categoriaMarcaRepository.findById(producto.id_categoriamarca());
        if(categoriamarcaoptional.isEmpty())throw new ResourceNotFoundException("CategoriaMarca no encontrado con id: " + producto.id_categoriamarca());
        Optional<SubCategoria> subCategoriaOptional = subCategoriaRepository.findById(producto.id_subcategoria());
        if(subCategoriaOptional.isEmpty()) throw new ResourceNotFoundException("SubCategoria no encontrado con id: " + producto.id_subcategoria());

        CategoriaMarca categoriamarca = categoriamarcaoptional.get();
        SubCategoria subCategoria = subCategoriaOptional.get();
        List<Archivo> archivos = new ArrayList<>();
        for (MultipartFile file : files) {
            archivos.add(s3service.uploadObject(file,"imagen_producto", "producto"));
        }
        Producto nuevo = new Producto().builder()
                .nombre(producto.nombre())
                .pn(producto.pn())
                .descripcion(producto.descripcion())
                .Stock(producto.stock())
                .precio(producto.precio())
                .categoriamarca(categoriamarca)
                .subcategoria(subCategoria)
                .garantia_cliente(producto.garantia_cliente())
                .garantia_total(producto.garantia_total())
                .archivo_Principal(s3service.uploadObject(fileprincipal,"imagen_producto", "producto"))
                .archivos(archivos)
                .build();

        if(categoriamarca.getProductos() == null){
            categoriamarca.setProductos(new ArrayList<>());
        }
        if(subCategoria.getProducto() == null){
            subCategoria.setProducto(new ArrayList<>());
        }
        subCategoria.getProducto().add(nuevo);
        categoriamarca.getProductos().add(nuevo);

        productoRepository.save(nuevo);
        subCategoriaRepository.save(subCategoria);
        categoriaMarcaRepository.save(categoriamarca);
    }

    private void uploadFile(MultipartFile file)throws IOException{
//        try {
//            String filename = file.getOriginalFilename();
//            AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region).build();
//            //PutObjectRequest putObjectrequest = new PutObjectRequest(namebucket,filename,file.getInputStream(), null);
//            ObjectMetadata metadata = new ObjectMetadata();
//            metadata.setContentLength(file.getSize());
//            s3.putObject(namebucket, filename, file.getInputStream(),metadata);
//        }catch (IOException e){
//            throw new IOException(e.getMessage());
//        }
    }
    public void update(ProductoRequest productoRequest, List<MultipartFile> files, MultipartFile fileprincipal) {
        Optional<Producto> productoOptional = productoRepository.findById(productoRequest.id());

        if (productoOptional.isEmpty()) throw new ResourceNotFoundException("Producto no encontrado con id: " + productoRequest.id());

        Producto productoActual = productoOptional.get();
        CategoriaMarca categoriaMarcaActual = productoActual.getCategoriamarca();
        SubCategoria subCategoriaActual = productoActual.getSubcategoria();

        // Actualizamos los datos del producto existente
        productoActual.setNombre(productoRequest.nombre());
        productoActual.setPn(productoRequest.pn());
        productoActual.setDescripcion(productoRequest.descripcion());
        productoActual.setStock(productoRequest.stock());
        productoActual.setPrecio(productoRequest.precio());
        productoActual.setGarantia_cliente(productoRequest.garantia_cliente());
        productoActual.setGarantia_total(productoRequest.garantia_total());

        // Verificamos si la categoría ha cambiado
        if (!categoriaMarcaActual.getId().equals(productoRequest.id_categoriamarca())) {
            // Removemos el producto de la lista de la categoría actual
            categoriaMarcaActual.getProductos().remove(productoActual);

            // Buscamos la nueva categoría
            Optional<CategoriaMarca> nuevaCategoriaOptional = categoriaMarcaRepository.findById(productoRequest.id_categoriamarca());

            if (nuevaCategoriaOptional.isEmpty()) throw new ResourceNotFoundException("Categoría nueva no encontrada con id: " + productoRequest.id_categoriamarca());

            CategoriaMarca nuevaCategoria = nuevaCategoriaOptional.get();

            // Asignamos la nueva categoría al producto
            productoActual.setCategoriamarca(nuevaCategoria);
            nuevaCategoria.getProductos().add(productoActual);

            // Guardamos la nueva categoría
            categoriaMarcaRepository.save(nuevaCategoria);
        }
        // Verificamos si la subcategoría ha cambiado
        if (!subCategoriaActual.getId().equals(productoRequest.id_subcategoria())) {
            // Removemos el producto de la lista de la subcategoría actual
            subCategoriaActual.getProducto().remove(productoActual);

            // Buscamos la nueva subcategoría
            Optional<SubCategoria> nuevaSubCategoriaOptional = subCategoriaRepository.findById(productoRequest.id_categoriamarca());

            if (nuevaSubCategoriaOptional.isEmpty()) throw new ResourceNotFoundException("SubCategoria nueva no encontrada con id: " + productoRequest.id_subcategoria());

            SubCategoria nuevaSubCategoria = nuevaSubCategoriaOptional.get();

            // Asignamos la nueva subcategoría al producto
            productoActual.setSubcategoria(nuevaSubCategoria);
            nuevaSubCategoria.getProducto().add(productoActual);

            // Guardamos la nueva subcategoría
            subCategoriaRepository.save(nuevaSubCategoria);
        }
        List<Archivo> eliminados = productoActual.getArchivos().stream().filter(p -> !productoRequest.imageurl().contains(p.getUrl())).collect(Collectors.toList());
        productoActual.getArchivos().removeAll(eliminados);

        Archivo principal = productoActual.getArchivo_Principal();
        if(fileprincipal != null && !fileprincipal.isEmpty()){

            productoActual.setArchivo_Principal(null);
            productoActual.setArchivo_Principal(s3service.uploadObject(fileprincipal,"imagen_producto", "producto"));
        }
        if(files != null && !files.isEmpty()){
            productoActual.getArchivos().addAll(s3service.uploadsObjects(files, "imagen_producto", "producto"));
        }

        // Guardamos el producto actualizado
        productoRepository.save(productoActual);

        // Guardamos la categoría actual si ha cambiado
        if (!categoriaMarcaActual.getId().equals(productoRequest.id_categoriamarca())) {
            categoriaMarcaRepository.save(categoriaMarcaActual);
        }
        // Guardamos la Subcategoría actual si ha cambiado
        if (!subCategoriaActual.getId().equals(productoRequest.id_subcategoria())) {
            subCategoriaRepository.save(subCategoriaActual);
        }
        if(principal != null){
            archivoRepository.delete(principal);
            s3service.deleteFile(principal);
        }
        if(!eliminados.isEmpty()){
            archivoRepository.deleteAll(eliminados);
            s3service.deleteFiles(eliminados);
        }

    }

    public void delete(String id) {
        Optional<Producto> productoOptional = productoRepository.findById(id);
        if (productoOptional.isEmpty()) {
            throw new ResourceNotFoundException("Producto no encontrado con id: " + id);
        }
        if(productoOptional.get().getLote() != null){
            throw new ResourceNotFoundException("No se puede eliminar un producto con lotes asociados: \n" +productoOptional.get().getLote().toString());
        }
        productoRepository.delete(productoOptional.get());
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
                producto.getArchivo_Principal() != null ? producto.getArchivo_Principal().getUrl() : "",
                producto.getArchivos().stream().map(Archivo::getUrl).collect(Collectors.toList())
        );
    }
    public List<ProductoResponse> Busqueda(String keyboard){
        List<Producto> productos = productoRepository.findByNombreOrDescripcionContaining(keyboard);
        return productos.stream().map(this::mapToProductoResponse).toList();
    }

    public List<ProductoResponse> getAllPaged(String search,List<String> marca, List<String> categoria,List<String> subcategoria, Pageable pageable) {

        Specification<Producto> specification = ProductoSpecifications.withFilters(marca, categoria, subcategoria, search);
        Page<Producto> productos = productoRepository.findAll(specification, pageable);

        return productos.stream().map(this::mapToProductoResponse).toList();
    }
}
