package com.Gropo06.turnos_medicos.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Gropo06.turnos_medicos.dto.UsuarioDTO;
import com.Gropo06.turnos_medicos.exceptions.UsuarioInvalido;
import com.Gropo06.turnos_medicos.mapper.MapperUtil;
import com.Gropo06.turnos_medicos.model.Usuario;
import com.Gropo06.turnos_medicos.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepo;

	// Devolvemos todos los usuarios como DTOs
	public List<UsuarioDTO> getAll() {
		return usuarioRepo.findAll().stream().map(MapperUtil::toDto).collect(Collectors.toList());
	}

	// Buscamos un usuario por ID, devolvemos DTO o null si no existe
	public UsuarioDTO findById(Long id) {
		return usuarioRepo.findById(id).map(MapperUtil::toDto).orElse(null);
	}

	// Buscamos usuario por email de contacto, devolvemos DTO o arrojamos una excepción si no existe
	public UsuarioDTO findByEmail(String email) {
		return usuarioRepo.findByContactoEmail(email).map(MapperUtil::toDto)
				.orElseThrow(() -> new UsuarioInvalido("Usuario no encontrado: " + email));
	}

	// Crea o actualiza usuario desde DTO
	public UsuarioDTO save(UsuarioDTO dto) {
		Usuario entidad = MapperUtil.toEntity(dto);
		Usuario guardado = usuarioRepo.save(entidad);
		return MapperUtil.toDto(guardado);
	}

	// Eliminamos usuario por ID
	public void deleteById(Long id) {
		usuarioRepo.deleteById(id);
	}
}
