package com.technet.backend.model.dto.globales;

import com.technet.backend.model.dto.documentos.TipoComprobanteResponse;
import com.technet.backend.model.entity.correlativos.TipoComprobante;
import com.technet.backend.model.entity.globales.TipoCondicion;
import com.technet.backend.model.entity.globales.TipoMoneda;
import com.technet.backend.model.entity.globales.TipoPago;

import java.util.List;

public record TipadoDocumentos(
        List<TipoComprobanteResponse> tipocomprobantes,
        List<TipoCondicion> tipocondiciones,
        List<TipoPago> tipopagos,
        List<TipoMoneda> tipomonedas
) {
}
