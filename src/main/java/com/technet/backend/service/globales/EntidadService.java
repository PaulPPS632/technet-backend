package com.technet.backend.service.globales;

import com.technet.backend.model.dto.EntidadRequest;
import com.technet.backend.model.entity.globales.Entidad;
import com.technet.backend.model.entity.globales.TipoEntidad;
import com.technet.backend.repository.globales.EntidadRepository;
import com.technet.backend.repository.globales.TipoEntidadRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntidadService {
    private final EntidadRepository entidadRepository;
    private final TipoEntidadRepository tipoEntidadRepository;
    public List<Entidad> getAll(){
        return entidadRepository.findAll();
    }

    public List<Entidad> getAllPaged(Pageable pageable){
        Page<Entidad> entidades = entidadRepository.findAll(pageable);
        return entidades.getContent();
    }

    public Entidad getById(String id){
        Optional<Entidad> entidadOptional = entidadRepository.findById(id);
        if(entidadOptional.isEmpty()) throw new EntityNotFoundException("No se Encontro la entidad con id: " + id);
        return entidadOptional.get();
    }

    public void save(EntidadRequest entidad){
        entidadRepository.save(mapToEntidad(entidad));
    }
    public void update(String id, EntidadRequest entidad){
        Optional<Entidad> entidadOptional = entidadRepository.findById(id);
        if(entidadOptional.isEmpty()) throw new EntityNotFoundException("No se Encontro la entidad con id: " + id);
        if(!entidadOptional.get().getTipoEntidad().equals(entidad.id_tipoEntidad())){
            Optional<TipoEntidad> tipoEntidadOptional = tipoEntidadRepository.findById(entidad.id_tipoEntidad());
            if(tipoEntidadOptional.isEmpty()) throw new EntityNotFoundException("No se Encontro la entidad nueva con id: " + id);

            entidadOptional.get().setNombre(entidad.nombre());
            entidadOptional.get().setDocumento(entidad.documento());
            entidadOptional.get().setDireccion(entidad.direccion());
            entidadOptional.get().setEmail(entidad.email());
            entidadOptional.get().setTelefono(entidad.telefono());
            entidadOptional.get().setTipoEntidad(tipoEntidadOptional.get());

            entidadRepository.save(entidadOptional.get());
        }
    }
    public void delete(String id){
        Optional<Entidad> entidadOptional = entidadRepository.findById(id);
        if(entidadOptional.isEmpty()) throw new EntityNotFoundException("No se Encontro la entidad con id: " + id);
        entidadRepository.deleteById(id);
    }
    private Entidad mapToEntidad(EntidadRequest entidadResponse){
        Optional<TipoEntidad> tipoEntidad = tipoEntidadRepository.findById(entidadResponse.id_tipoEntidad());
        if(tipoEntidad.isEmpty())  throw new EntityNotFoundException("No se Encontro el Tipo entidad con id: " + entidadResponse.id_tipoEntidad());
        return new Entidad().builder()
                .nombre(entidadResponse.nombre())
                .documento(entidadResponse.documento())
                .direccion(entidadResponse.direccion())
                .telefono(entidadResponse.telefono())
                .email(entidadResponse.email())
                .tipoEntidad(tipoEntidad.get())
                .build();
    }

    public List<Entidad> getByIdDocumento(String documento) {
        List<Entidad> entidades = entidadRepository.findByDocumentoContaining(documento);
        if(entidades.isEmpty()) throw  new EntityNotFoundException("No se encontro la entidad con documento: " + documento);
        return entidades;
    }
    public void EntidadRegisterJson(String documento, String nombre, String direccion, String telefono,String email, String tipoEntidad){
        Optional<Entidad> entidadOptional = entidadRepository.findByDocumento(documento);
        if(entidadOptional.isEmpty()){
            Entidad nuevo = new Entidad();
            nuevo.setDocumento(documento);
            nuevo.setNombre(nombre);
            nuevo.setDireccion(direccion);
            nuevo.setTelefono(telefono);
            nuevo.setEmail(email);
            if(tipoEntidad == "DNI"){
                Optional<TipoEntidad> tipo = tipoEntidadRepository.findById(1L);
                nuevo.setTipoEntidad(tipo.orElseThrow());
            }
            entidadRepository.save(nuevo);
        }
    }
}
