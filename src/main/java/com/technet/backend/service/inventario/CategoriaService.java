package com.technet.backend.service.inventario;

import com.technet.backend.exception.ResourceNotFoundException;
import com.technet.backend.model.dto.inventario.*;
import com.technet.backend.model.entity.inventario.Categoria;
import com.technet.backend.model.entity.inventario.SubCategoria;
import com.technet.backend.repository.inventario.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public List<CategoriaResponse> getAllPaged(Pageable pageable){
        Page<Categoria> categoria = categoriaRepository.findAll(pageable);
        return categoria.stream().map(this::mapToCategoriaResponse).toList();
    }
    public List<CategoriaResponse> getAll(){
        List<Categoria> categoria = categoriaRepository.findAll();
        return categoria.stream().map(this::mapToCategoriaResponse).toList();
    }
    public CategoriaResponse getById(Long id){
        Optional<Categoria> categoria = categoriaRepository.findById(id);

        if(categoria.isEmpty()) throw new EntityNotFoundException("Categoria no encontrada con el id: " + id);

        return mapToCategoriaResponse(categoria.get());
    }


    public void save(CategoriaRequest categoriaRequest){
        Categoria categoria = mapToCategoria(categoriaRequest);
        categoriaRepository.save(categoria);
    }
    public void savesAll(CategoriasRequest marcasRequest){
        categoriaRepository.saveAll(marcasRequest.categorias().stream().map(this::mapToCategoria).toList());
    }
    public void update(Long id, CategoriaRequest categoriaRequest) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        if(categoria.isEmpty()) throw new EntityNotFoundException("Categoria no encontrada con el id: " + id);
        Categoria actual = categoria.get();
        actual.setNombre(categoriaRequest.nombre());
        actual.setDescripcion(categoriaRequest.descripcion());
        categoriaRepository.save(actual);
    }
    public void delete(Long id){
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        if(categoria.isEmpty()) throw new EntityNotFoundException("Categoria no encontrada con el id: " + id);
        categoriaRepository.delete(categoria.get());
    }

    private CategoriaResponse mapToCategoriaResponse(Categoria categoria){
        return new CategoriaResponse(categoria.getId(), categoria.getNombre(), categoria.getDescripcion(),categoria.getSubacategorias().stream().map(this::mapCategoriaMarcaResponse).toList());
    }
    private SubCategoriaResponse mapCategoriaMarcaResponse(SubCategoria subCategoria){
        return new SubCategoriaResponse(subCategoria.getId(), subCategoria.getNombre(), subCategoria.getDescripcion(),subCategoria.getCategoria().getNombre());
    }
    private Categoria mapToCategoria(CategoriaRequest categoriaRequest){
        return new Categoria().builder().nombre(categoriaRequest.nombre()).Descripcion(categoriaRequest.descripcion()).build();
    }
    private SubCategoriaResponse mapSubCategoriaResponse(SubCategoria subCategoria){
        return new SubCategoriaResponse(subCategoria.getId(), subCategoria.getNombre(), subCategoria.getDescripcion(),subCategoria.getCategoria().getNombre());
    }
    public List<SubCategoriaResponse> SubCategoriaBelogns(Long id) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        if(categoria.isEmpty()) throw new ResourceNotFoundException("Categoria no encontrada con id: " + categoria.get().getId());

        return categoria.get().getSubacategorias().stream().map(this::mapSubCategoriaResponse).toList();
    }
}
