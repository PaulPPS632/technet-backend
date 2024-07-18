package com.technet.backend.controller.inventario;

import com.technet.backend.model.dto.inventario.PedidosReStockRequest;
import com.technet.backend.model.dto.inventario.PedidosReStockResponse;
import com.technet.backend.service.inventario.PedidosReStockService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory/pedidos")
@RequiredArgsConstructor
public class PedidoReStockController {
    private final PedidosReStockService pedidosReStockService;

    @PostMapping
    public void registrar(@RequestBody PedidosReStockRequest pedidosReStockRequest){
        pedidosReStockService.registrar(pedidosReStockRequest);
    }

    @GetMapping
    public List<PedidosReStockResponse> listar(){
        return pedidosReStockService.Lista();
    }
}
