package com.Gropo06.turnos_medicos.service;

import com.Gropo06.turnos_medicos.model.Usuario;
import com.Gropo06.turnos_medicos.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

/*Carga el usuario Paciente o Empleado desde la BD usando el email y le asigna ROLE_<NOMBRE_DEL_ROL>*/

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UsuarioRepository usuarioRepo;

	public CustomUserDetailsService(UsuarioRepository usuarioRepo) {
		this.usuarioRepo = usuarioRepo;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// Buscamos el Usuario Paciente o Empleado por el campo contacto.email
		Usuario us = usuarioRepo.findByContactoEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

		return new User(us.getContacto().getEmail(), us.getPassword(),
				Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + us.getRol().getNombre())));
	}
}

