package com.technet.backend.controller.documentos;

import com.technet.backend.model.dto.documentos.CompraResponse;
import com.technet.backend.model.dto.documentos.RegistrarCompraRequest;
import com.technet.backend.service.documentos.RegistroCompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory/registrocompra")
@RequiredArgsConstructor
public class RegistroCompraController {
    private final RegistroCompraService registroCompraService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registrar(@RequestBody RegistrarCompraRequest registrarCompraRequest){
        registroCompraService.registrar(registrarCompraRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompraResponse> Listar(){
        return registroCompraService.Lista();
    }
}
