package com.Gropo06.turnos_medicos.repository;

import com.Gropo06.turnos_medicos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerfilRepository extends JpaRepository<Usuario, Long> {

    // Busca una persona por el email que está en su contacto
    Optional<Usuario> findByContactoEmail(String email);

}