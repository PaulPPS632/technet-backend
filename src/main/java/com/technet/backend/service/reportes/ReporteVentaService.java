package com.technet.backend.service.reportes;

import com.technet.backend.model.dto.reportes.VentaReporteDto;
import com.technet.backend.model.entity.documentos.Venta;
import com.technet.backend.repository.documentos.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteVentaService {
    private final VentaRepository ventaRepository;

    public List<VentaReporteDto> reporte(){
        List<Venta> ventas = ventaRepository.findAll();
        return ventas.stream().map(this::mapToVentaReporteDto).toList();
    }
    private VentaReporteDto mapToVentaReporteDto(Venta venta){
        return VentaReporteDto.builder()
                .fecha(venta.getFecha_emision())
                .usuario_id(venta.getUsuario().getId())
                .usuario_name(venta.getUsuario().getName())
                .monto(venta.getTotal())
                .build();
    }
}
