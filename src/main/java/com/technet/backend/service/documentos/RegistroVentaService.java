package com.technet.backend.service.documentos;

import com.technet.backend.model.dto.documentos.CorrelativoResponse;
import com.technet.backend.model.dto.documentos.RegistrarVentaRequest;
import com.technet.backend.model.dto.documentos.VentaResponse;
import com.technet.backend.model.dto.users.PrivilegioResponse;
import com.technet.backend.model.dto.users.RolResponse;
import com.technet.backend.model.dto.users.UserResponse;
import com.technet.backend.model.entity.correlativos.Correlativo;
import com.technet.backend.model.entity.documentos.DetalleVenta;
import com.technet.backend.model.entity.documentos.Venta;
import com.technet.backend.model.entity.globales.Entidad;
import com.technet.backend.model.entity.globales.TipoCondicion;
import com.technet.backend.model.entity.globales.TipoMoneda;
import com.technet.backend.model.entity.globales.TipoPago;
import com.technet.backend.model.entity.historiales.HistorialVenta;
import com.technet.backend.model.entity.inventario.EstadoProducto;
import com.technet.backend.model.entity.inventario.Producto;
import com.technet.backend.model.entity.inventario.ProductoSerie;
import com.technet.backend.model.entity.users.Privilegio;
import com.technet.backend.model.entity.users.Rol;
import com.technet.backend.model.entity.users.User;
import com.technet.backend.repository.correlativos.TipoCondicionRepository;
import com.technet.backend.repository.correlativos.TipoMonedaRepository;
import com.technet.backend.repository.correlativos.TipoPagoRepository;
import com.technet.backend.repository.documentos.DetalleVentaRepository;
import com.technet.backend.repository.documentos.VentaRepository;
import com.technet.backend.repository.historiales.HistorialVentaRepository;
import com.technet.backend.repository.inventario.EstadoProductoRepository;
import com.technet.backend.repository.inventario.ProductoRepository;
import com.technet.backend.repository.inventario.ProductoSerieRepository;
import com.technet.backend.repository.users.UserRepository;
import com.technet.backend.service.correlativos.CorrelativoService;
import com.technet.backend.service.globales.EntidadService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegistroVentaService {

    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;

    private final TipoCondicionRepository tipoCondicionRepository;
    private final TipoPagoRepository tipoPagoRepository;
    private final TipoMonedaRepository tipoMonedaRepository;
    private final ProductoSerieRepository productoSerieRepository;
    private final EntidadService entidadService;
    private final CorrelativoService correlativoService;
    private final UserRepository userRepository;
    private final HistorialVentaRepository historialVentaRepository;
    private final EstadoProductoRepository estadoProductoRepository;
    private final ProductoRepository productoRepository;

    public void registrar(RegistrarVentaRequest registrarVentaRequest) {
        Optional<TipoCondicion> tipoCondicionOptional = tipoCondicionRepository.findById(registrarVentaRequest.id_tipocondicion());
        Optional<TipoPago> tipoPagoOptional = tipoPagoRepository.findById(registrarVentaRequest.id_tipopago());
        Optional<TipoMoneda> tipoMonedaOptional = tipoMonedaRepository.findById(registrarVentaRequest.id_tipomoneda());
        Optional<User> usuarioOptional = userRepository.findByUsername(registrarVentaRequest.usuario_id());
        if(tipoCondicionOptional.isEmpty() || tipoPagoOptional.isEmpty() || tipoMonedaOptional.isEmpty()) throw new EntityNotFoundException("Existe un error alguno de los tipos enviados");
        List<Entidad> entidades = entidadService.getByIdDocumento(registrarVentaRequest.documento_cliente());

        //Correlativo correlativo = generateCorrelativo();

        Venta venta = new Venta();
        Correlativo corr = correlativoService.generateCorrelativoEntity(registrarVentaRequest.prefijo(),registrarVentaRequest.numeracion());
        venta.setCorrelativo(corr);
        venta.setEntidad_cliente(entidades.get(0));
        venta.setUsuario(usuarioOptional.orElseThrow());
        venta.setTipocondicion(tipoCondicionOptional.orElseThrow());
        venta.setTipopago(tipoPagoOptional.orElseThrow());
        venta.setTipomoneda(tipoMonedaOptional.orElseThrow());
        venta.setTipo_cambio(registrarVentaRequest.tipo_cambio());
        venta.setFecha_emision(registrarVentaRequest.fecha_emision());
        venta.setFecha_vencimiento(registrarVentaRequest.fecha_vencimiento());
        venta.setNota(registrarVentaRequest.nota());
        venta.setGravada(registrarVentaRequest.gravada());
        venta.setImpuesto(registrarVentaRequest.impuesto());
        venta.setTotal(registrarVentaRequest.total());
        venta.setFechapago(registrarVentaRequest.fechapago());
        venta.setFormapago(registrarVentaRequest.formapago());
        List<ProductoSerie> seriesRegistradas = new ArrayList<>();
        List<DetalleVenta> detalleVentas = registrarVentaRequest.detalles().stream().flatMap(
                request -> request.series().stream().map(
                        serie -> {
                            ProductoSerie productoSerie = productoSerieRepository.findBySn(serie)
                                    .orElseThrow(() -> {
                                        correlativoService.delete(corr);
                                        throw new RuntimeException("ProductoSerie no encontrado para SN: " + serie);
                                    });
                            seriesRegistradas.add(productoSerie);
                            Producto producto =productoSerie.getLote().getProducto();
                            producto.setStock(producto.getStock()-1);
                            productoRepository.save(producto);
                            return DetalleVenta.builder()
                                    .venta(venta)
                                    .productoserie(productoSerie)
                                    .precio_neto(request.precio_unitario())
                                    .build();
                        }
                )
        ).collect(Collectors.toList());

        venta.setDetalleVenta(new ArrayList<>());
        ventaRepository.save(venta);
        detalleVentaRepository.saveAll(detalleVentas);

        venta.getDetalleVenta().addAll(detalleVentas);
        Venta Venta_Guardada = ventaRepository.save(venta);
        historialVentaRepository.save(HistorialVenta.builder().fecha(LocalDateTime.now()).accion("Registro").detalles("").venta(Venta_Guardada).usuario(usuarioOptional.orElseThrow()).build());
        cambiarEstado(seriesRegistradas);
    }
    private void cambiarEstado(List<ProductoSerie> seriesRegistradas){
        EstadoProducto venta = estadoProductoRepository.findById(2L).orElseThrow();
        seriesRegistradas.forEach(serie -> {
            serie.setEstadoproducto(venta);
            productoSerieRepository.save(serie);
        });
    }
    public List<VentaResponse> Lista(){
        List<Venta> ventas = ventaRepository.findAll();
        return ventas.stream().map(this::maptoVentaResponse).toList();
    }
    private VentaResponse maptoVentaResponse(Venta venta){
        return VentaResponse.builder()
                .id(venta.getId())
                .correlativo(maptoCorrelativoResponse(venta.getCorrelativo()))
                .cliente(venta.getEntidad_cliente())
                .usuario(maptoUserResponse(venta.getUsuario()))
                .tipocondicion(venta.getTipocondicion())
                .tipopago(venta.getTipopago())
                .tipomoneda(venta.getTipomoneda())
                .tipo_cambio(venta.getTipo_cambio())
                .fecha_emision(venta.getFecha_emision())
                .fecha_vencimiento(venta.getFecha_vencimiento())
                .nota(venta.getNota())
                .gravada(venta.getGravada())
                .impuesto(venta.getImpuesto())
                .total(venta.getTotal())
                .fechapago(venta.getFechapago())
                .formapago(venta.getFormapago())
                .build();
    }
    private CorrelativoResponse maptoCorrelativoResponse(Correlativo correlativo){
        String prefij = correlativo.getNumeracioncomprobante().getTipocomprobante().getPrefijo();
        Long num = correlativo.getNumeracioncomprobante().getNumeracion();
        Long corr = correlativo.getNumero();
        return CorrelativoResponse.builder()
                .prefijo(prefij)
                .numeracion(num)
                .correlativo(corr)
                .documento(prefij + "00" + num + "-"+corr)
                .build();
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
}
