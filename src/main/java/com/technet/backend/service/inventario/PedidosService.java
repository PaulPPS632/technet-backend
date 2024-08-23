package com.technet.backend.service.inventario;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.technet.backend.model.dto.inventario.PedidosReStockRequest;
import com.technet.backend.model.dto.inventario.PedidosReStockResponse;
import com.technet.backend.model.dto.inventario.ProductoResponse;
import com.technet.backend.model.dto.users.PrivilegioResponse;
import com.technet.backend.model.dto.users.RolResponse;
import com.technet.backend.model.dto.users.UserResponse;
import com.technet.backend.model.entity.globales.Archivo;
import com.technet.backend.model.entity.globales.Entidad;
import com.technet.backend.model.entity.globales.TipoEntidad;
import com.technet.backend.model.entity.inventario.Pedidos;
import com.technet.backend.model.entity.inventario.Producto;
import com.technet.backend.model.entity.users.Privilegio;
import com.technet.backend.model.entity.users.Rol;
import com.technet.backend.model.entity.users.User;
import com.technet.backend.repository.globales.EntidadRepository;
import com.technet.backend.repository.inventario.PedidosReStockRepository;
import com.technet.backend.repository.inventario.ProductoRepository;
import com.technet.backend.repository.users.UserRepository;
import com.technet.backend.service.globales.EntidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidosService {

    private final PedidosReStockRepository pedidosReStockRepository;
    private final ObjectMapper objectMapper;
    private final EntidadService entidadService;
    public void registrar(Pedidos pedido){
        try {
            JsonNode rootNode = objectMapper.readTree(pedido.getDatospago());
            JsonNode billingDetails = rootNode.path("customer")
                    .path("billingDetails");
            String IdentityCode = billingDetails.path("identityCode").asText();
            pedido.setFecha(LocalDateTime.now());
            pedidosReStockRepository.save(pedido);
            entidadService.EntidadRegisterJson(IdentityCode,
                    billingDetails.path("firstName").asText() +" " +billingDetails.path("lastName").asText(),
                    billingDetails.path("address").asText(),
                    billingDetails.path("cellPhoneNumber").asText(),
                    rootNode.path("customer").path("email").asText(),
                    "DNI"
            );

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }



    }
    public ResponseEntity<List<Pedidos>> Lista(){
        List<Pedidos> pedidos = pedidosReStockRepository.findTop100ByOrderByFechaDesc();
        if(pedidos.isEmpty()){
            return ResponseEntity.status(500).body(new ArrayList<>());
        }
        return ResponseEntity.ok(pedidos);
    }
    public void editar(PedidosReStockRequest pedido){
        Pedidos actual = pedidosReStockRepository.findById(pedido.id()).orElseThrow();
        pedidosReStockRepository.save(Pedidos.builder()
                .id(actual.getId())
                .fecha(pedido.fecha())
                .estado(pedido.estado())
                .build());
    }
    private PedidosReStockResponse mapToPedidosReStockResponse(Pedidos pedido){
        return PedidosReStockResponse.builder()
                .id(pedido.getId())
                .fecha(pedido.getFecha())
                .estado(pedido.getEstado())
                .build();
    }
    public ProductoResponse mapToProductoResponse(Producto producto){
        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getPn(),
                producto.getDescripcion(),
                producto.getStock(),
                producto.getPrecio(),
                producto.getCategoriamarca().getMarca().getNombre(),
                producto.getCategoriamarca().getNombre(),
                producto.getSubcategoria().getCategoria().getNombre(),
                producto.getSubcategoria().getNombre(),
                producto.getGarantia_cliente(),
                producto.getGarantia_total(),
                producto.getArchivo_Principal() != null ? producto.getArchivo_Principal().getUrl() : "",
                producto.getArchivos().stream().map(Archivo::getUrl).collect(Collectors.toList())
        );
    }
    private UserResponse maptoUserResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getUsername())
                .email(user.getEmail())
                .regist(user.isRegist())
                .rol(maptoRolResponse(user.getRol()))
                .build();
    }
    private RolResponse maptoRolResponse(Rol rol){
        if(rol != null){
            return RolResponse.builder()
                    .id(rol.getId())
                    .nombre(rol.getNombre())
                    .descripcion(rol.getDescripcion())
                    .privilegios(rol.getPrivilegios().stream().map(this::maptoPrivilegioResponse).toList())
                    .build();
        }
        return RolResponse.builder().build();
    }
    private PrivilegioResponse maptoPrivilegioResponse(Privilegio privilegio){
        if(privilegio != null){
            return PrivilegioResponse.builder()
                    .id(privilegio.getId())
                    .nombre(privilegio.getNombre())
                    .descripcion(privilegio.getDescripcion())
                    .build();
        }
        return PrivilegioResponse.builder().build();
    }

    public ResponseEntity<List<Pedidos>> getPedidosByUsername(String username) {
        return ResponseEntity.ok(pedidosReStockRepository.findByUsername(username));
    }

    public void cambios(Pedidos pedido) {
        pedidosReStockRepository.save(pedido);
    }
}
