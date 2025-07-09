package com.Gropo06.turnos_medicos.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Gropo06.turnos_medicos.dto.EspecialidadDTO;
import com.Gropo06.turnos_medicos.mapper.MapperUtil;
import com.Gropo06.turnos_medicos.model.Especialidad;
import com.Gropo06.turnos_medicos.repository.EspecialidadRepository;

@Service
public class EspecialidadService {

    @Autowired
    private EspecialidadRepository repo;

    
     // Devolvemos todas las Especialidades como DTOs.
    public List<EspecialidadDTO> getAll() {
        return repo.findAll()
                   .stream()
                   .map(MapperUtil::toDto)
                   .collect(Collectors.toList());
    }

    
     //Buscamos una Especialidad por id y la devolvemos como DTO.
    public EspecialidadDTO buscarEspecialidadPorId(Long id) {
        return repo.findById(id)
                   .map(MapperUtil::toDto)
                   .orElse(null);
    }

    
     //Crea/actualiza una Especialidad a partir de un DTO y lo devuelve el DTO resultante con el id asignado     
    public EspecialidadDTO save(EspecialidadDTO dto) {
        Especialidad entidad = MapperUtil.toEntity(dto);
        Especialidad guardada = repo.save(entidad);
        return MapperUtil.toDto(guardada);
    }

    
     //Elimina una Especialidad por su id.
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
    
    //Validamos duplicados por nombre
    public boolean existsByNombre(String nombre) {
        return repo.existsByNombre(nombre);
    }
}
