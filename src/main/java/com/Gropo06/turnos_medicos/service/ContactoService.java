package com.Gropo06.turnos_medicos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Gropo06.turnos_medicos.dto.ContactoDTO;
import com.Gropo06.turnos_medicos.exceptions.ContactoInvalido;
import com.Gropo06.turnos_medicos.mapper.MapperUtil;
import com.Gropo06.turnos_medicos.model.Contacto;
import com.Gropo06.turnos_medicos.repository.ContactoRepository;

@Service
public class ContactoService {

    @Autowired
    private ContactoRepository repo;

    // Buscamos un contacto por ID y devolvemos su DTO
    public ContactoDTO findById(Long id) {
        return repo.findById(id).map(MapperUtil::toDto)
                .orElseThrow(() -> new ContactoInvalido("Contacto no encontrado: " + id));
    }

    // Crea/actualiza un contacto a partir de su DTO
    public ContactoDTO save(ContactoDTO dto) {
        Contacto ent = MapperUtil.toEntity(dto);
        Contacto saved = repo.save(ent);
        return MapperUtil.toDto(saved);
    }

    // Validamos si existe un email ya registrado
    public boolean existsByEmail(String email) {
        return repo.existsByEmail(email);
    }

    // Obtenemos el idUsuario a partir del email (devolvemos null si no existe)
    public Long getIdUsuarioByEmail(String email) {
        return repo.findIdUsuarioByEmail(email);
    }
}
