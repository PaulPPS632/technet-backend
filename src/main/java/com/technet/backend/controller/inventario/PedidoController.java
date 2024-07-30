package com.technet.backend.controller.inventario;

import com.technet.backend.model.dto.inventario.PedidosReStockRequest;
import com.technet.backend.model.dto.inventario.PedidosReStockResponse;
import com.technet.backend.model.entity.inventario.Pedidos;
import com.technet.backend.service.inventario.PedidosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.List;

@RestController
@RequestMapping("/inventory/pedidos")
@RequiredArgsConstructor
public class PedidoController {
    private final PedidosService pedidosReStockService;

    @PostMapping
    public void registrar(@RequestBody Pedidos pedido){
        pedidosReStockService.registrar(pedido);
    }

    @GetMapping
    public ResponseEntity<List<Pedidos>> listar(){

        return pedidosReStockService.Lista();
    }
}
