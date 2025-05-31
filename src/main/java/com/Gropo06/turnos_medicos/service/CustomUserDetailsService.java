package com.Gropo06.turnos_medicos.service;

import com.Gropo06.turnos_medicos.model.Usuario;
import com.Gropo06.turnos_medicos.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepo;

    public CustomUserDetailsService(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario us = usuarioRepo.findByContactoEmail(email).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        // Crea un User de spring con email, pass y rol
        return new User(
        		us.getContacto().getEmail(),
        		us.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + us.getRol().getNombre()))
        );
    }
}
