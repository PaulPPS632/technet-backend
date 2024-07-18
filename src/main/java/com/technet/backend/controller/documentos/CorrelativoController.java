package com.technet.backend.controller.documentos;

import com.technet.backend.model.dto.documentos.CorrelativoResponse;
import com.technet.backend.service.correlativos.CorrelativoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory/correlativo")
@RequiredArgsConstructor
public class CorrelativoController {

    private final CorrelativoService correlativoService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CorrelativoResponse generate(@RequestParam String prefijo, @RequestParam Long numeracion){
        return correlativoService.generateCorrelativoResponse(prefijo,numeracion);
    }

    @GetMapping("/siguiente")
    @ResponseStatus(HttpStatus.OK)
    public Long siguiente(@RequestParam String prefijo, @RequestParam Long numeracion){
        return correlativoService.correlativoSiguiente(prefijo,numeracion);
    }
}
