package com.Gropo06.turnos_medicos.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Gropo06.turnos_medicos.dto.PacienteDTO;
import com.Gropo06.turnos_medicos.exceptions.PacienteInvalido;
import com.Gropo06.turnos_medicos.mapper.MapperUtil;
import com.Gropo06.turnos_medicos.model.Paciente;
import com.Gropo06.turnos_medicos.repository.PacienteRepository;

@Service
public class PacienteService {

	@Autowired
	private PacienteRepository pacienteRepo;

	// Devolvemos todos los pacientes como DTOs
	public List<PacienteDTO> getAll() {
		return pacienteRepo.findAll().stream().map(MapperUtil::toDto).collect(Collectors.toList());
	}

	// Buscamos un paciente por su ID y devolvemos su DTO (o null si no existe)
	public PacienteDTO findById(Long id) {
		return pacienteRepo.findById(id).map(MapperUtil::toDto).orElse(null);
	}

	// Buscamos un paciente por su email de contacto y devuelve su DTO. Arroja
	// PacienteInvalido si no lo encuentra
	public PacienteDTO findByEmail(String email) {
		return pacienteRepo.findByContactoEmail(email).map(MapperUtil::toDto)
				.orElseThrow(() -> new PacienteInvalido("Paciente no encontrado: " + email));
	}

	// Guarda (crea o actualiza) un paciente a partir de su DTO y devuelve el DTO resultante
	public PacienteDTO save(PacienteDTO dto) {
		Paciente entidad = MapperUtil.toEntity(dto);
		Paciente guardado = pacienteRepo.save(entidad);
		return MapperUtil.toDto(guardado);
	}

	// Eliminamos un paciente por su ID
	public void deleteById(Long id) {
		pacienteRepo.deleteById(id);
	}
}
