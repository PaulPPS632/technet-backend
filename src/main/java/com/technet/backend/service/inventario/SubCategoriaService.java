package com.technet.backend.service.inventario;

import com.technet.backend.model.dto.inventario.CategoriaMarcaRequest;
import com.technet.backend.model.dto.inventario.SubCategoriaRequest;
import com.technet.backend.model.dto.inventario.SubCategoriaResponse;
import com.technet.backend.model.dto.inventario.SubCategoriasRequest;
import com.technet.backend.model.entity.inventario.Categoria;
import com.technet.backend.model.entity.inventario.CategoriaMarca;
import com.technet.backend.model.entity.inventario.SubCategoria;
import com.technet.backend.repository.inventario.CategoriaRepository;
import com.technet.backend.repository.inventario.SubCategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubCategoriaService {

    private final SubCategoriaRepository subCategoriaRepository;
    private final CategoriaRepository categoriaRepository;

    public List<SubCategoriaResponse> getAll() {
        List<SubCategoria> subCategorias = subCategoriaRepository.findAll();
        return subCategorias.stream().map(this::mapToSubCategoriaResponse).toList();
    }

    public SubCategoriaResponse getById(Long id) {
        Optional<SubCategoria> subCategoria = subCategoriaRepository.findById(id);

        if(subCategoria.isEmpty()) throw new EntityNotFoundException("Subcategoria no encontrada con el id: " + id);

        return mapToSubCategoriaResponse(subCategoria.get());
    }

    public void save(SubCategoriaRequest subCategoriaRequest){

        Optional<Categoria> categoria = categoriaRepository.findById(subCategoriaRequest.id_categoria());

        if(categoria.isEmpty()) throw new EntityNotFoundException("Categoria no encontrada con el id: " + subCategoriaRequest.id_categoria());

        SubCategoria subCategoria = new SubCategoria().builder()
                .nombre(subCategoriaRequest.nombre())
                .descripcion(subCategoriaRequest.descripcion())
                .categoria(categoria.get())
                .build();

        if(categoria.get().getSubacategorias() == null){
            categoria.get().setSubacategorias(new ArrayList<>());
        }
        categoria.get().getSubacategorias().add(subCategoria);

        subCategoriaRepository.save(subCategoria);
        categoriaRepository.save(categoria.get());


    }
    public void savesAll(SubCategoriasRequest subCategoriasRequest){
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(subCategoriasRequest.id_categoria());
        if (categoriaOptional.isEmpty()) throw new EntityNotFoundException("Categoria no encontrada con el id: " + subCategoriasRequest.id_categoria());

        Categoria categoria = categoriaOptional.get();
        if (categoria.getSubacategorias() == null) {
            categoria.setSubacategorias(new ArrayList<>());
        }
        List<SubCategoria> nuevasSubCategorias = subCategoriasRequest.subCategorias().stream()
                .map(request -> SubCategoria.builder()
                        .nombre(request.nombre())
                        .descripcion(request.descripcion())
                        .categoria(categoria)
                        .build())
                .collect(Collectors.toList());

        categoria.getSubacategorias().addAll(nuevasSubCategorias);
        subCategoriaRepository.saveAll(nuevasSubCategorias);
        categoriaRepository.save(categoria);
    }


    public void delete(Long id) {
        Optional<SubCategoria> subCategoria = subCategoriaRepository.findById(id);
        if(subCategoria.isEmpty()) throw new EntityNotFoundException("Categoria no encontrada con el id: " + id);
        if(subCategoria.get().getProducto() == null || subCategoria.get().getProducto().isEmpty()){
            subCategoriaRepository.delete(subCategoria.get());
        }else{
            throw new EntityNotFoundException("No se puede eliminar una subcategoria con productos asociados: " + id);
        }
    }

    public void update(Long id, SubCategoriaRequest subcategoria) {
        Optional<SubCategoria> actualsubcategoriaoptional = subCategoriaRepository.findById(id);

        if(actualsubcategoriaoptional.isEmpty())throw new EntityNotFoundException("Categoría no encontrada");

        SubCategoria subcategoriaactual = actualsubcategoriaoptional.get();
        Categoria categoriaactual = subcategoriaactual.getCategoria();

        subcategoriaactual.setNombre(subcategoria.nombre());
        subcategoriaactual.setDescripcion(subcategoria.descripcion());
        /*
        if(!categoriaactual.getId().equals(subcategoria.id_categoria())){
            actual.setNombre(subcategoria.nombre());
            subCategoriaRepository.save(actual);

        }else {
            categoriaactual.getSubacategorias().remove(actual);
            categoriaRepository.save(categoriaactual);
            Optional<Marca> nuevamarcaoptional = marcaRepository.findById(categoria.id_marca());
            if(nuevamarcaoptional.isPresent()){
                Marca nuevamarca = nuevamarcaoptional.get();
                actual.setNombre(categoria.nombre());
                actual.setMarca(nuevamarca);

                if(nuevamarca.getCategoriamarcas() == null){
                    nuevamarca.setCategoriamarcas(new ArrayList<>());
                }
                nuevamarca.getCategoriamarcas().add(actual);
                categoriaMarcaRepository.save(actual);
                marcaRepository.save(nuevamarca);
            }else {
                throw new EntityNotFoundException("Nueva marca no encontrada");
            }
        }*/
        if(!categoriaactual.getId().equals(subcategoria.id_categoria())){
            //Removemos la subcategoria de la lista de la categoria actual
            categoriaactual.getSubacategorias().remove(subcategoria);

            //Buscamos la nueva Categoria
            Optional<Categoria> nuevaCategoriaOptional = categoriaRepository.findById(subcategoria.id_categoria());
            if(nuevaCategoriaOptional.isEmpty()) throw new EntityNotFoundException("Categoría nueva no encontrada con id: " + subcategoria.id_categoria());

            Categoria nuevaCategoria = nuevaCategoriaOptional.get();

            // Asignamos la nueva Categoria a la SubCategoria
            subcategoriaactual.setCategoria(nuevaCategoria);
            nuevaCategoria.getSubacategorias().add(subcategoriaactual);

            // Guardamos la nueva subcategoria
            subCategoriaRepository.save(subcategoriaactual);
        }
        subCategoriaRepository.save(subcategoriaactual);

        if(!categoriaactual.getId().equals(subcategoria.id_categoria())){
            categoriaRepository.save(categoriaactual);
        }

    }

    private SubCategoriaResponse mapToSubCategoriaResponse(SubCategoria subCategoria){
        return new SubCategoriaResponse(subCategoria.getId(), subCategoria.getNombre(), subCategoria.getDescripcion(), subCategoria.getCategoria().getNombre());
    }
    private CategoriaMarca mapToCategoriaMarca(CategoriaMarcaRequest categoriaMarcaRequest){
        return new CategoriaMarca().builder().nombre(categoriaMarcaRequest.nombre()).build();
    }
}
