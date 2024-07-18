package com.technet.backend.service.globales;

import com.technet.backend.model.dto.documentos.TipoComprobanteResponse;
import com.technet.backend.model.dto.globales.TipadoDocumentos;
import com.technet.backend.model.entity.correlativos.TipoComprobante;
import com.technet.backend.model.entity.globales.TipoCondicion;
import com.technet.backend.model.entity.globales.TipoMoneda;
import com.technet.backend.model.entity.globales.TipoPago;
import com.technet.backend.repository.correlativos.TipoComprobanteRepository;
import com.technet.backend.repository.correlativos.TipoCondicionRepository;
import com.technet.backend.repository.correlativos.TipoMonedaRepository;
import com.technet.backend.repository.correlativos.TipoPagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TipadoService {

    private final TipoComprobanteRepository tipoComprobanteRepository;
    private final TipoCondicionRepository tipoCondicionRepository;
    private final TipoPagoRepository tipoPagoRepository;
    private final TipoMonedaRepository tipoMonedaRepository;

    public TipadoDocumentos get() {
        List<TipoComprobante> tipoComprobante = tipoComprobanteRepository.findAll();
        List<TipoCondicion> tipoCondicion = tipoCondicionRepository.findAll();
        List<TipoPago> tipoPago = tipoPagoRepository.findAll();
        List<TipoMoneda> tipoMoneda = tipoMonedaRepository.findAll();

        TipadoDocumentos nuevo = new TipadoDocumentos(tipoComprobante.stream().map(this::mapToTipoComprobanteResponse).toList(),tipoCondicion,tipoPago,tipoMoneda);
        return nuevo;
    }
    private TipoComprobanteResponse mapToTipoComprobanteResponse(TipoComprobante tipoComprobante) {
        return new TipoComprobanteResponse(tipoComprobante.getId(), tipoComprobante.getPrefijo(), tipoComprobante.getDescripcion());
    }
}
