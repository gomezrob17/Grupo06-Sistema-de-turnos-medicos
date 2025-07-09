package com.Gropo06.turnos_medicos.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Gropo06.turnos_medicos.dto.EstadoTurnoDTO;
import com.Gropo06.turnos_medicos.mapper.MapperUtil;
import com.Gropo06.turnos_medicos.repository.EstadoTurnoRepository;

@Service
public class EstadoTurnoService {

	@Autowired
	private EstadoTurnoRepository estadoTurnoRepository;

	// Buscamos un EstadoTurno por nombre y devuelve su DTO
	public EstadoTurnoDTO obtenerPorNombre(String nombre) {
		return estadoTurnoRepository.findByNombre(nombre).map(MapperUtil::toDto)
				.orElseThrow(() -> new RuntimeException("EstadoTurno no encontrado: " + nombre));
	}

	// Devolvemos el Estado CANCELADO como DTO
	public EstadoTurnoDTO obtenerEstadoCancelado() {
		return obtenerPorNombre("CANCELADO");
	}

	// Listamos todos los estados de turno como DTO
	public List<EstadoTurnoDTO> listarTodos() {
		return estadoTurnoRepository.findAll().stream().map(MapperUtil::toDto).collect(Collectors.toList());
	}
}
