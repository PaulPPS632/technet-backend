package com.technet.backend.controller.inventario;

import com.technet.backend.service.inventario.ProductoService;
import com.technet.backend.model.dto.inventario.ProductoRequest;
import com.technet.backend.model.dto.inventario.ProductoResponse;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/inventory/producto")
@RequiredArgsConstructor
public class ProductoController {
    private final ProductoService productoService;
    
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductoResponse> getAll(){
        return productoService.getAll();
    }

    @GetMapping("/paged")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductoResponse> getAllMarca(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) List<String> marca,
            @RequestParam(required = false) List<String> categoria,
            @RequestParam(required = false) List<String> subcategoria,
            Pageable pageable) {

        return productoService.getAllPaged(search, marca, categoria, subcategoria, pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductoResponse getById(@PathVariable("id") String id){
        return productoService.getById(id);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductoResponse> getByKey(@RequestParam("keyboard") String keyboard){return productoService.Busqueda(keyboard);}

    @PostMapping(consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.CREATED)
    public void save(
            @RequestPart("producto") ProductoRequest producto,
            @RequestPart("files") List<MultipartFile> files,
            @RequestPart("fileprincipal") MultipartFile fileprincipal){
        productoService.save(producto, files, fileprincipal);
    }

    @PutMapping(consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.OK)
    public void update(
            @RequestPart("producto") ProductoRequest producto,
            @RequestPart("files") List<MultipartFile> files,
            @RequestPart("fileprincipal") MultipartFile fileprincipal
    ){
        productoService.update(producto, files,fileprincipal);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void delete(@PathVariable("id") String id){
        productoService.delete(id);
    }
}
