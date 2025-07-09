package com.Gropo06.turnos_medicos.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Gropo06.turnos_medicos.dto.SucursalDTO;
import com.Gropo06.turnos_medicos.mapper.MapperUtil;
import com.Gropo06.turnos_medicos.model.Sucursal;
import com.Gropo06.turnos_medicos.repository.SucursalRepository;

@Service
public class SucursalService {

	@Autowired
	private SucursalRepository repo;

	// Devolvemos todas las sucursales como DTOs
	public List<SucursalDTO> getAll() {
		return repo.findAll().stream().map(MapperUtil::toDto).collect(Collectors.toList());
	}

	// Buscamos una sucursal por ID y devuelve su DTO (o null si no existe)
	public SucursalDTO findById(Long id) {
		return repo.findById(id).map(MapperUtil::toDto).orElse(null);
	}

	// Guarda (crea o actualiza) una sucursal a partir de su DTO y devuelve el DTO resultante
	public SucursalDTO save(SucursalDTO dto) {
		Sucursal ent = MapperUtil.toEntity(dto);
		Sucursal saved = repo.save(ent);
		return MapperUtil.toDto(saved);
	}

	// Eliminamos una sucursal por su ID
	public void deleteById(Long id) {
		repo.deleteById(id);
	}

	// Verificamos si existe una sucursal con ese nombre
	public boolean existsByName(String nombre) {
		return repo.existsByNombre(nombre);
	}

	// Devolvemos las sucursales que ofrecen la especialidad indicada
	public List<SucursalDTO> findByEspecialidadId(Long especialidadId) {
		return repo.findDistinctByProfesionalesEspecialidadId(especialidadId).stream().map(MapperUtil::toDto)
				.collect(Collectors.toList());
	}

	// Buscamos varias sucursales por sus IDs
	public List<SucursalDTO> buscarSucursalesPorIds(List<Long> ids) {
		return repo.findAllById(ids).stream().map(MapperUtil::toDto).collect(Collectors.toList());
	}
	
	public boolean existeNombreEnOtraSucursal(String nombre, Long idSucursalActual) {
		Sucursal encontrada = repo.findByNombre(nombre);
		if (encontrada == null)
			return false;
		if (idSucursalActual != null && encontrada.getIdSucursal().equals(idSucursalActual))
			return false;
		return true;
	}
}
