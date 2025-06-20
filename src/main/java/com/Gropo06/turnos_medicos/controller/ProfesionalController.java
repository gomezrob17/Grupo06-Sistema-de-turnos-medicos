package com.Gropo06.turnos_medicos.controller;

import com.Gropo06.turnos_medicos.exceptions.EmailExistente;
import com.Gropo06.turnos_medicos.exceptions.EspecialidadInvalida;
import com.Gropo06.turnos_medicos.exceptions.ProfesionalInvalido;
import com.Gropo06.turnos_medicos.exceptions.RolNoEncontrado;
import com.Gropo06.turnos_medicos.exceptions.SucursalInvalida;
import com.Gropo06.turnos_medicos.exceptions.TurnoYaAsignado;
import com.Gropo06.turnos_medicos.model.Contacto;
import com.Gropo06.turnos_medicos.model.Disponibilidad;
import com.Gropo06.turnos_medicos.model.Especialidad;
import com.Gropo06.turnos_medicos.model.Profesional;
import com.Gropo06.turnos_medicos.model.Rol;
import com.Gropo06.turnos_medicos.model.Sucursal;
import com.Gropo06.turnos_medicos.repository.ContactoRepository;
import com.Gropo06.turnos_medicos.repository.EspecialidadRepository;
import com.Gropo06.turnos_medicos.repository.ProfesionalRepository;
import com.Gropo06.turnos_medicos.repository.RolRepository;
import com.Gropo06.turnos_medicos.repository.SucursalRepository;
import com.Gropo06.turnos_medicos.repository.TurnoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/empleado/profesionales")
public class ProfesionalController {

    private final ProfesionalRepository profesionalRepo;
    private final EspecialidadRepository especialidadRepo;
    private final SucursalRepository sucursalRepo;
    private final RolRepository rolRepo;
    private final ContactoRepository contactoRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private TurnoRepository turnoRepo;

    public ProfesionalController(ProfesionalRepository profesionalRepo, EspecialidadRepository especialidadRepo,
                                 SucursalRepository sucursalRepo, RolRepository rolRepo,
                                 ContactoRepository contactoRepo, PasswordEncoder passwordEncoder) {
        this.profesionalRepo = profesionalRepo;
        this.especialidadRepo = especialidadRepo;
        this.sucursalRepo = sucursalRepo;
        this.rolRepo = rolRepo;
        this.contactoRepo = contactoRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // Muestra la lista de profesionales + formulario vacío para crear uno nuevo.
    @GetMapping
    public String listarProfesionales(Model model) {
        model.addAttribute("profesionales", profesionalRepo.findAll());
        model.addAttribute("profesionalForm", new Profesional());
        model.addAttribute("especialidadesList", especialidadRepo.findAll());
        model.addAttribute("sucursalesList", sucursalRepo.findAll());
        return "empleado/profesionales";
    }

    // Guardar Profesional (nuevo o edición)
    @PostMapping("/save")
    public String guardarProfesional(@ModelAttribute("profesionalForm") Profesional profForm,
                                     @RequestParam("email") String email,
                                     @RequestParam("telefono") String telefono,
                                     @RequestParam("idEspecialidad") Long idEsp,
                                     @RequestParam("sucursalesSeleccionadas") List<Long> sucursalesIds,
                                     Model model) {

        // Validación de edad
        LocalDate fechaNacimiento = profForm.getFechaNacimiento();
        LocalDate hoy = LocalDate.now();

        if (fechaNacimiento == null) {
            model.addAttribute("errorFecha", "La fecha de nacimiento es obligatoria.");
            return recargarVista(model, profForm);
        }

        if (fechaNacimiento.isAfter(hoy)) {
            model.addAttribute("errorFecha", "La fecha de nacimiento no puede ser futura.");
            return recargarVista(model, profForm);
        }

        if (fechaNacimiento.isAfter(hoy.minusYears(18))) {
            model.addAttribute("errorFecha", "El profesional debe ser mayor de 18 años.");
            return recargarVista(model, profForm);
        }

        Profesional prof;
        boolean esEdicion = profForm.getIdUsuario() != null;

        if (esEdicion) {
            // Edición
            prof = profesionalRepo.findByIdWithDisponibilidades(profForm.getIdUsuario())
                    .orElseThrow(() -> new ProfesionalInvalido("Profesional inválido Id: " + profForm.getIdUsuario()));

            prof.setNombre(profForm.getNombre());
            prof.setApellido(profForm.getApellido());
            prof.setDni(profForm.getDni());
            prof.setFechaNacimiento(fechaNacimiento);
            prof.setGenero(profForm.getGenero());
            prof.setMatricula(profForm.getMatricula());

            Contacto contactoExistente = prof.getContacto();
            contactoExistente.setEmail(email);
            contactoExistente.setTelefono(telefono);
            contactoRepo.save(contactoExistente);

            Especialidad esp = especialidadRepo.findById(idEsp)
                    .orElseThrow(() -> new EspecialidadInvalida("Especialidad inválida Id: " + idEsp));
            prof.setEspecialidad(esp);

            Set<Sucursal> sucursalesSet = sucursalesIds.stream()
                    .map(id -> sucursalRepo.findById(id)
                            .orElseThrow(() -> new SucursalInvalida("Sucursal inválida Id: " + id)))
                    .collect(Collectors.toSet());
            prof.setSucursales(sucursalesSet);

            prof.getDisponibilidades().clear();
            procesarDisponibilidades(prof, profForm.getDisponibilidades());

            profesionalRepo.save(prof);

        } else {
            // Nuevo profesional
            prof = new Profesional();
            prof.setNombre(profForm.getNombre());
            prof.setApellido(profForm.getApellido());
            prof.setDni(profForm.getDni());
            prof.setFechaNacimiento(fechaNacimiento);
            prof.setGenero(profForm.getGenero());
            prof.setMatricula(profForm.getMatricula());

            Contacto nuevoContacto = new Contacto(email, telefono);
            if (contactoRepo.existsByEmail(nuevoContacto.getEmail())) {
                throw new EmailExistente("Email ya existente");
            }
            contactoRepo.save(nuevoContacto);
            prof.setContacto(nuevoContacto);

            Rol rolProfesional = rolRepo.findByNombre("PROFESIONAL")
                    .orElseThrow(() -> new RolNoEncontrado("Rol PROFESIONAL no encontrado"));
            prof.setRol(rolProfesional);

            prof.setPassword(passwordEncoder.encode(profForm.getMatricula()));

            Especialidad espNueva = especialidadRepo.findById(idEsp)
                    .orElseThrow(() -> new EspecialidadInvalida("Especialidad inválida Id: " + idEsp));
            prof.setEspecialidad(espNueva);

            Set<Sucursal> sucursalesSet = sucursalesIds.stream()
                    .map(id -> sucursalRepo.findById(id)
                            .orElseThrow(() -> new SucursalInvalida("Sucursal inválida Id: " + id)))
                    .collect(Collectors.toSet());
            prof.setSucursales(sucursalesSet);

            procesarDisponibilidades(prof, profForm.getDisponibilidades());

            profesionalRepo.save(prof);
        }

        return "redirect:/empleado/profesionales";
    }

    // Procesa lista de disponibilidades (nuevas o editadas)
    private void procesarDisponibilidades(Profesional prof, List<Disponibilidad> listaDispDelFormulario) {
        if (listaDispDelFormulario != null) {
            String solapamientoMensaje = validarSinSolapamientos(listaDispDelFormulario);
            if (solapamientoMensaje != null) {
                throw new RuntimeException(solapamientoMensaje);
            }

            for (Disponibilidad dForm : listaDispDelFormulario) {
                Sucursal suc = sucursalRepo.findById(dForm.getSucursal().getIdSucursal())
                        .orElseThrow(() -> new SucursalInvalida("Sucursal inválida Id: " + dForm.getSucursal().getIdSucursal()));
                dForm.setSucursal(suc);
                dForm.setProfesional(prof);
                prof.getDisponibilidades().add(dForm);
            }
        }
    }

    // Valida que no haya solapamientos entre disponibilidades
    private String validarSinSolapamientos(List<Disponibilidad> listaDisp) {
        listaDisp.sort((a, b) -> {
            int cmp = a.getFecha().compareTo(b.getFecha());
            if (cmp != 0)
                return cmp;
            return a.getHoraInicio().compareTo(b.getHoraInicio());
        });

        for (int i = 0; i < listaDisp.size(); i++) {
            Disponibilidad d1 = listaDisp.get(i);
            for (int j = i + 1; j < listaDisp.size(); j++) {
                Disponibilidad d2 = listaDisp.get(j);

                if (!d1.getFecha().equals(d2.getFecha())) {
                    break;
                }

                if (!(d1.getHoraFin().isBefore(d2.getHoraInicio()) || d2.getHoraFin().isBefore(d1.getHoraInicio()))) {
                    return String.format("Conflicto: para el día %s, el horario %s–%s se superpone con %s–%s",
                            d1.getFecha(), d1.getHoraInicio(), d1.getHoraFin(), d2.getHoraInicio(), d2.getHoraFin());
                }
            }
        }
        return null;
    }

    // Carga formulario de edición
    @GetMapping("/edit/{id}")
    public String editarProfesional(@PathVariable("id") Long id, Model model) {
        Profesional prof = profesionalRepo.findByIdWithDisponibilidades(id)
                .orElseThrow(() -> new ProfesionalInvalido("Profesional inválido Id: " + id));

        if (prof.getDisponibilidades() == null) {
            prof.setDisponibilidades(new ArrayList<>());
        }

        // Lazy initialization de sucursales
        prof.getSucursales().size();

        model.addAttribute("profesionalForm", prof);
        model.addAttribute("profesionales", profesionalRepo.findAll());
        model.addAttribute("especialidadesList", especialidadRepo.findAll());
        model.addAttribute("sucursalesList", sucursalRepo.findAll());

        return "empleado/profesionales";
    }

    // Eliminar profesional
    @GetMapping("/delete/{id}")
    public String eliminarProfesional(@PathVariable("id") Long id) {
        if (turnoRepo.existsByProfesional_IdUsuario(id)) {
            throw new TurnoYaAsignado("El profesional posee Turnos asignados.");
        }
        profesionalRepo.deleteById(id);
        return "redirect:/empleado/profesionales";
    }

    // Método utilitario para recargar vista en caso de error
    private String recargarVista(Model model, Profesional profForm) {
        model.addAttribute("profesionales", profesionalRepo.findAll());
        model.addAttribute("especialidadesList", especialidadRepo.findAll());
        model.addAttribute("sucursalesList", sucursalRepo.findAll());
        model.addAttribute("profesionalForm", profForm);
        return "empleado/profesionales";
    }
}