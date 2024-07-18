package com.technet.backend.repository.correlativos;

import com.technet.backend.model.entity.correlativos.NumeracionComprobante;
import com.technet.backend.model.entity.correlativos.TipoComprobante;
import com.technet.backend.model.entity.inventario.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NumeracionComprobanteRepository extends JpaRepository<NumeracionComprobante, Long> {

    Optional<NumeracionComprobante> findByTipocomprobanteAndNumeracion(TipoComprobante tipocomprobante, Long numeracion);

    //@Query("SELECT nc FROM NumeracionComprobante nc WHERE nc.tipocomprobante.id = :id_tipocomprobante AND nc.numeracion = :numeracion")
    //Optional<NumeracionComprobante> buscar(@Param("numeracion") Long numeracion, @Param("id_tipocomprobante") Long id_tipocomprobante);

    //@Query("SELECT nc FROM NumeracionComprobante nc WHERE nc.tipocomprobante = :id_tipocomprobante AND nc.numeracion = :numeracion")
    //Optional<NumeracionComprobante> findByTipocomprobanteAndNumeracion(@Param("numeracion") Long numeracion, @Param("id_tipocomprobante") Long id_tipocomprobante);

}
