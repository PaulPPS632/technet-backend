package com.technet.backend.controller.globales;

import com.technet.backend.model.dto.globales.TipadoDocumentos;
import com.technet.backend.service.globales.TipadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("inventory/tipado")
@RequiredArgsConstructor
public class TipadoController {

    private final TipadoService tipadoService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public TipadoDocumentos get(){
        return tipadoService.get();
    }
}
