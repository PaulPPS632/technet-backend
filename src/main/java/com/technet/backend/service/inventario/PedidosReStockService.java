package com.technet.backend.service.inventario;
import com.technet.backend.model.dto.inventario.PedidosReStockRequest;
import com.technet.backend.model.dto.inventario.PedidosReStockResponse;
import com.technet.backend.model.dto.inventario.ProductoResponse;
import com.technet.backend.model.dto.users.PrivilegioResponse;
import com.technet.backend.model.dto.users.RolResponse;
import com.technet.backend.model.dto.users.UserResponse;
import com.technet.backend.model.entity.globales.Archivo;
import com.technet.backend.model.entity.inventario.PedidosReStock;
import com.technet.backend.model.entity.inventario.Producto;
import com.technet.backend.model.entity.users.Privilegio;
import com.technet.backend.model.entity.users.Rol;
import com.technet.backend.model.entity.users.User;
import com.technet.backend.repository.inventario.PedidosReStockRepository;
import com.technet.backend.repository.inventario.ProductoRepository;
import com.technet.backend.repository.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidosReStockService {

    private final PedidosReStockRepository pedidosReStockRepository;
    private final UserRepository userRepository;
    private final ProductoRepository productoRepository;

    public void registrar(PedidosReStockRequest pedido){
        pedidosReStockRepository.save(PedidosReStock.builder()
                        .usuario(userRepository.findById(pedido.id_usuario()).orElseThrow())
                        .fecha(pedido.fecha())
                        .producto(productoRepository.findById(pedido.id_producto()).orElseThrow())
                        .estado("pendiente")
                        .cantidad(pedido.cantidad())
                        .nota(pedido.nota())

                .build());
    }
    public List<PedidosReStockResponse> Lista(){
        List<PedidosReStock> pedidos = pedidosReStockRepository.findAll();
        if(pedidos.isEmpty()){
            return new ArrayList<>();
        }
        return pedidos.stream().map(this::mapToPedidosReStockResponse).toList();
    }
    public void editar(PedidosReStockRequest pedido){
        PedidosReStock actual = pedidosReStockRepository.findById(pedido.id()).orElseThrow();
        pedidosReStockRepository.save(PedidosReStock.builder()
                .id(actual.getId())
                .usuario(userRepository.findById(pedido.id_usuario()).orElseThrow())
                .fecha(pedido.fecha())
                .producto(productoRepository.findById(pedido.id_producto()).orElseThrow())
                .estado(pedido.estado())
                .cantidad(pedido.cantidad())
                .nota(pedido.nota())
                .build());
    }
    private PedidosReStockResponse mapToPedidosReStockResponse(PedidosReStock pedido){
        return PedidosReStockResponse.builder()
                .id(pedido.getId())
                .usuario(maptoUserResponse(pedido.getUsuario()))
                .fecha(pedido.getFecha())
                .producto(mapToProductoResponse(pedido.getProducto()))
                .estado(pedido.getEstado())
                .cantidad(pedido.getCantidad())
                .nota(pedido.getNota())
                .tenantId(pedido.getTenantId())
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
                .sub(user.getSub())
                .name(user.getName())
                .given_name(user.getGiven_name())
                .family_name(user.getFamily_name())
                .picture(user.getPicture())
                .email(user.getEmail())
                .email_verified(user.isEmail_verified())
                .locale(user.getLocale())
                .tenantName(user.getTenantName())
                .regist(user.isRegist())
                .tiponegocio(user.getTiponegocio())
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
}
