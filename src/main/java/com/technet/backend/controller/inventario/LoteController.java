package com.technet.backend.controller.inventario;

import com.technet.backend.model.dto.inventario.LoteRequest;
import com.technet.backend.model.dto.inventario.LoteResponse;
import com.technet.backend.service.inventario.LoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory/lote")
@RequiredArgsConstructor
public class LoteController {
    private final LoteService loteService;

    @GetMapping
    public List<LoteResponse> getAll(){
        return loteService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LoteResponse getById(@PathVariable("id") Long id){
        return loteService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody LoteRequest loteRequest){
        loteService.save(loteRequest);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") Long id, @RequestBody LoteRequest loteRequest) {
        loteService.update(id, loteRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void delete(@PathVariable("id") Long id){
        loteService.delete(id);

    }
    
}
