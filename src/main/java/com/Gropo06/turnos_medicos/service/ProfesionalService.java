package com.Gropo06.turnos_medicos.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Gropo06.turnos_medicos.dto.ProfesionalDTO;
import com.Gropo06.turnos_medicos.dto.SucursalDTO;
import com.Gropo06.turnos_medicos.exceptions.ProfesionalInvalido;
import com.Gropo06.turnos_medicos.mapper.MapperUtil;
import com.Gropo06.turnos_medicos.model.Especialidad;
import com.Gropo06.turnos_medicos.model.Profesional;
import com.Gropo06.turnos_medicos.model.Sucursal;
import com.Gropo06.turnos_medicos.repository.ProfesionalRepository;

@Service
public class ProfesionalService {

	@Autowired
	private ProfesionalRepository repo;
	@Autowired
	private SucursalService sucursalService;
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

	        // Antes de actualizar, limpiamos especialidades en sus sucursales antiguas
	        Especialidad especialidadAnterior = entidad.getEspecialidad();
	        Set<Sucursal> sucursalesAnteriores = new HashSet<>(entidad.getSucursales());

	        MapperUtil.updateEntity(entidad, dto);

	        for (Sucursal sucursal : sucursalesAnteriores) {
	            Sucursal sucursalCompleta = sucursalService.obtenerSucursalEntidadPorId(sucursal.getIdSucursal());

	            boolean otroProfesionalConMismaEspecialidad = sucursalCompleta.getProfesionales().stream()
	                .filter(p -> !p.getIdUsuario().equals(dto.getIdUsuario())) // Ignorar el que estamos actualizando
	                .anyMatch(p -> p.getEspecialidad().getIdEspecialidad().equals(especialidadAnterior.getIdEspecialidad()));

	            if (!otroProfesionalConMismaEspecialidad) {
	                sucursalCompleta.getEspecialidades().remove(especialidadAnterior);
	                sucursalService.guardarSucursalEntidad(sucursalCompleta);
	            }
	        }

	    } else {
	        String passwordTemporal = UUID.randomUUID().toString().substring(0, 8);
	        String passwordEncriptado = passwordEncoder.encode(passwordTemporal);
	        dto.setPassword(passwordEncriptado);
	        entidad = MapperUtil.toEntity(dto);
	    }

	    Profesional guardado = repo.save(entidad);

	    // Asegurar que la especialidad esté en todas las sucursales nuevas
	    if (guardado.getSucursales() != null && guardado.getEspecialidad() != null) {
	        for (Sucursal sucursal : guardado.getSucursales()) {
	            Sucursal sucursalGestionada = sucursalService.obtenerSucursalEntidadPorId(sucursal.getIdSucursal());
	            sucursalGestionada.getEspecialidades().add(guardado.getEspecialidad());
	            sucursalService.guardarSucursalEntidad(sucursalGestionada);
	        }
	    }

	    return MapperUtil.toDto(guardado);
	}

	// Eliminamos un profesional por su ID
	public void deleteById(Long id) {
		repo.deleteById(id);
	}
}
