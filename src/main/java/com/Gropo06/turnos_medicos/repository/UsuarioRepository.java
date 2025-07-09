package com.Gropo06.turnos_medicos.repository;

import com.Gropo06.turnos_medicos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Buscamos usuario por el email de contacto
    Optional<Usuario> findByContactoEmail(String email);
}
