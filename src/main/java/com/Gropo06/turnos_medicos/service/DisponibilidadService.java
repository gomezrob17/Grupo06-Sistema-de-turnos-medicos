package com.Gropo06.turnos_medicos.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Gropo06.turnos_medicos.dto.DisponibilidadDTO;
import com.Gropo06.turnos_medicos.dto.EspecialidadDTO;
import com.Gropo06.turnos_medicos.dto.SucursalDTO;
import com.Gropo06.turnos_medicos.mapper.MapperUtil;
import com.Gropo06.turnos_medicos.model.Disponibilidad;
import com.Gropo06.turnos_medicos.repository.DisponibilidadRepository;

@Service
public class DisponibilidadService {

	@Autowired
	private DisponibilidadRepository repo;

	@Autowired
	private EspecialidadService especialidadService;

	@Autowired
	private SucursalService sucursalService;

	// Listamos todas las disponibilidades como DTOs
	public List<DisponibilidadDTO> getAll() {
		return repo.findAll().stream().map(MapperUtil::toDto).collect(Collectors.toList());
	}

	// Buscamos por ID y devolvemos DTO (o null si no existe)
	public DisponibilidadDTO findById(Long id) {
		return repo.findById(id).map(MapperUtil::toDto).orElse(null);
	}

	// Creamos o actualizamos una disponibilidad desde su DTO
	public DisponibilidadDTO save(DisponibilidadDTO dto) {
		Disponibilidad ent = MapperUtil.toEntity(dto);
		Disponibilidad saved = repo.save(ent);
		return MapperUtil.toDto(saved);
	}

	// Eliminamos una disponibilidad por ID
	public void deleteById(Long id) {
		repo.deleteById(id);
	}

	// Busca disponibilidades filtrando por especialidad, sucursal y rango de fechas.
	public List<DisponibilidadDTO> findByFilter(Long idEspecialidad, Long idSucursal, LocalDate desde,
			LocalDate hasta) {

		// 1) Obtenemos DTOs y convertimos a entidades
		EspecialidadDTO espDto = especialidadService.buscarEspecialidadPorId(idEspecialidad);
		SucursalDTO sucDto = sucursalService.findById(idSucursal);
		var espEnt = MapperUtil.toEntity(espDto);
		var sucEnt = MapperUtil.toEntity(sucDto);

		// 2) Consultamos repo y mapeamos los resultados
		return repo.findByProfesional_EspecialidadAndSucursalAndFechaBetween(espEnt, sucEnt, desde, hasta).stream()
				.map(MapperUtil::toDto).collect(Collectors.toList());
	}
}
