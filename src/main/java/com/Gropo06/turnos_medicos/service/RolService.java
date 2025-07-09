package com.Gropo06.turnos_medicos.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Gropo06.turnos_medicos.dto.RolDTO;
import com.Gropo06.turnos_medicos.exceptions.RolNoEncontrado;
import com.Gropo06.turnos_medicos.mapper.MapperUtil;
import com.Gropo06.turnos_medicos.repository.RolRepository;

@Service
public class RolService {

	@Autowired
	private RolRepository repo;

	// Listamos todos los roles como DTOs
	public List<RolDTO> getAll() {
		return repo.findAll().stream().map(MapperUtil::toDto).collect(Collectors.toList());
	}

	// Buscamos un rol por nombre y devuelve su DTO
	public RolDTO findByName(String nombre) {
		return repo.findByNombre(nombre).map(MapperUtil::toDto)
				.orElseThrow(() -> new RolNoEncontrado("Rol no encontrado: " + nombre));
	}
}
