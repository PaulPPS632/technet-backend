package com.technet.backend.controller.globales;

import com.technet.backend.model.dto.inventario.ProductoRequest;
import com.technet.backend.service.globales.ArchivoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("inventory/archivos")
@RequiredArgsConstructor
public class ArchivoController {
    private final ArchivoService archivoService;

    @GetMapping("/publicitaria")
    public Map<String, List<String>> ImagenesPublicitarias(){
        return archivoService.ImagenesPublicitarias();
    }

    @PostMapping(consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.CREATED)
    public void save(
            @RequestPart("tipo") String tipo,
            @RequestPart("files") List<MultipartFile> files){
        archivoService.save(tipo, files);
    }
}
