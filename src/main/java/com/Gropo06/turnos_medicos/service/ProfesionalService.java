package com.Gropo06.turnos_medicos.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Gropo06.turnos_medicos.dto.ProfesionalDTO;
import com.Gropo06.turnos_medicos.exceptions.ProfesionalInvalido;
import com.Gropo06.turnos_medicos.mapper.MapperUtil;
import com.Gropo06.turnos_medicos.model.Profesional;
import com.Gropo06.turnos_medicos.repository.ProfesionalRepository;

@Service
public class ProfesionalService {

	@Autowired
	private ProfesionalRepository repo;
	@Autowired
	private PasswordEncoder passwordEncoder;

	// Devolvemos todos los profesionales como DTOs
	public List<ProfesionalDTO> getAll() {
		return repo.findAll().stream().map(MapperUtil::toDto).collect(Collectors.toList());
	}

	// Buscamos un profesional por su ID y devuelve su DTO (o null si no existe)

	public ProfesionalDTO findById(Long id) {
		return repo.findById(id).map(MapperUtil::toDto).orElse(null);
	}

	// Guarda (crea o actualiza) un profesional a partir de su DTO y devuelve el DTO resultante
	public ProfesionalDTO save(ProfesionalDTO dto) {
	    Profesional entidad;

	    if (dto.getIdUsuario() != null) {
	        entidad = repo.findById(dto.getIdUsuario())
	            .orElseThrow(() -> new ProfesionalInvalido("Profesional no encontrado"));

	        MapperUtil.updateEntity(entidad, dto);
	    } else {
	        String passwordTemporal = UUID.randomUUID().toString().substring(0, 8);
	        String passwordEncriptado = passwordEncoder.encode(passwordTemporal);
	        dto.setPassword(passwordEncriptado);
	        entidad = MapperUtil.toEntity(dto);
	    }

	    // --- LOG DE SUCURSALES ASOCIADAS ---
	    if (entidad.getSucursales() == null) {
	        System.out.println("Sucursal Set ES NULL en entidad!!!");
	    } else {
	        System.out.println("Sucursales a guardar: " + entidad.getSucursales().size());
	        for (var s : entidad.getSucursales()) {
	            System.out.println("Sucursal ID: " + s.getIdSucursal());
	        }
	    }
	    // -----------------------------------

	    Profesional guardado = repo.save(entidad);

	    return MapperUtil.toDto(guardado);
	}

	// Eliminamos un profesional por su ID
	public void deleteById(Long id) {
		repo.deleteById(id);
	}
}
