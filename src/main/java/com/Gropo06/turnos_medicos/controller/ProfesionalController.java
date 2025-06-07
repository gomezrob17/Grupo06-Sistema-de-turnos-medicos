package com.Gropo06.turnos_medicos.controller;

import com.Gropo06.turnos_medicos.exceptions.CustomException;
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
			SucursalRepository sucursalRepo, RolRepository rolRepo, ContactoRepository contactoRepo,
			PasswordEncoder passwordEncoder) {
		this.profesionalRepo = profesionalRepo;
		this.especialidadRepo = especialidadRepo;
		this.sucursalRepo = sucursalRepo;
		this.rolRepo = rolRepo;
		this.contactoRepo = contactoRepo;
		this.passwordEncoder = passwordEncoder;
	}

	// Muestra la lista de profesionales + formulario (vacio) para crear uno nuevo.
	// Además se carga la lista con todas las Especialidades y Sucursales para los
	// select.

	@GetMapping
	public String listarProfesionales(Model model) {
		List<Profesional> lista = profesionalRepo.findAll();
		model.addAttribute("profesionales", lista);

		model.addAttribute("profesionalForm", new Profesional());

		List<Especialidad> especialidades = especialidadRepo.findAll();
		model.addAttribute("especialidadesList", especialidades);

		List<Sucursal> sucursales = sucursalRepo.findAll();
		model.addAttribute("sucursalesList", sucursales);

		return "empleado/profesionales";
	}

	// Guardar Profesional o actualizar uno existente.
	// En el form se incluyen los datos de contacto (email, teléfono) y, si es una
	// nueva creación asignamos el rol “PROFESIONAL” y generamos una contraseña por
	// defecto.

	@PostMapping("/save")
	public String guardarProfesional(@ModelAttribute("profesionalForm") Profesional profForm,
			@RequestParam("email") String email, @RequestParam("telefono") String telefono,
			@RequestParam("idEspecialidad") Long idEsp,
			@RequestParam("sucursalesSeleccionadas") List<Long> sucursalesIds, Model model) {
		Profesional prof;
		boolean esEdicion = profForm.getIdUsuario() != null;

		if (esEdicion) {
			prof = profesionalRepo.findByIdWithDisponibilidades(profForm.getIdUsuario()).orElseThrow(
					() -> new CustomException("Profesional inválido Id: " + profForm.getIdUsuario()));

			// Actualizamos campos especificos del usuario Profesionl
			prof.setNombre(profForm.getNombre());
			prof.setApellido(profForm.getApellido());
			prof.setDni(profForm.getDni());
			prof.setFechaNacimiento(profForm.getFechaNacimiento());
			prof.setGenero(profForm.getGenero());
			prof.setMatricula(profForm.getMatricula());

			// Actualizamos Contacto
			Contacto contactoExistente = prof.getContacto();
			contactoExistente.setEmail(email);
			contactoExistente.setTelefono(telefono);
			contactoRepo.save(contactoExistente);

			// Actualizamos Especialidad
			Especialidad esp = especialidadRepo.findById(idEsp)
					.orElseThrow(() -> new CustomException("Especialidad inválida Id: " + idEsp));
			prof.setEspecialidad(esp);

			// Actualizamos Sucursales
			Set<Sucursal> sucursalesSet = sucursalesIds.stream()
					.map(id -> sucursalRepo.findById(id)
							.orElseThrow(() -> new CustomException("Sucursal inválida Id: " + id)))
					.collect(Collectors.toSet());
			prof.setSucursales(sucursalesSet);

			// Gestión de Disponibilidades
			// Limpiamos la lista actual para luego rearmarla con lo que vino del form:
			prof.getDisponibilidades().clear();

			List<Disponibilidad> listaDispDelFormulario = profForm.getDisponibilidades();
			if (listaDispDelFormulario != null) {

				String solapamientoMensaje = validarSinSolapamientos(listaDispDelFormulario);
				if (solapamientoMensaje != null) {

					model.addAttribute("errorDisp", solapamientoMensaje);

					model.addAttribute("profesionales", profesionalRepo.findAll());
					model.addAttribute("especialidadesList", especialidadRepo.findAll());
					model.addAttribute("sucursalesList", sucursalRepo.findAll());
					model.addAttribute("profesionalForm", profForm);

					return "empleado/profesionales";
				}

				// Si pasa la validación, asignamos cada Disponibilidad al Profesional:
				for (Disponibilidad dForm : listaDispDelFormulario) {

					Sucursal suc = sucursalRepo.findById(dForm.getSucursal().getIdSucursal())
							.orElseThrow(() -> new CustomException(
									"Sucursal inválida Id: " + dForm.getSucursal().getIdSucursal()));
					dForm.setSucursal(suc);

					dForm.setProfesional(prof);
					prof.getDisponibilidades().add(dForm);
				}
			}

			profesionalRepo.save(prof);

		} else {
			// Nuevo Profesional
			prof = new Profesional();
			prof.setNombre(profForm.getNombre());
			prof.setApellido(profForm.getApellido());
			prof.setDni(profForm.getDni());
			prof.setFechaNacimiento(profForm.getFechaNacimiento());
			prof.setGenero(profForm.getGenero());
			prof.setMatricula(profForm.getMatricula());

			// Nuevo Contacto
			Contacto nuevoContacto = new Contacto(email, telefono);
			if (contactoRepo.existsByEmail(nuevoContacto.getEmail())) {
			    throw new CustomException("Email ya existente");
			}
			contactoRepo.save(nuevoContacto);
			prof.setContacto(nuevoContacto);

			// Rol PROFESIONAL
			Rol rolProfesional = rolRepo.findByNombre("PROFESIONAL")
					.orElseThrow(() -> new CustomException("Rol PROFESIONAL no encontrado"));
			prof.setRol(rolProfesional);

			// Contraseña por defecto (por ejemplo matrícula)
			String rawPass = profForm.getMatricula();
			prof.setPassword(passwordEncoder.encode(rawPass));

			// Asociar Especialidad
			Especialidad espNueva = especialidadRepo.findById(idEsp)
					.orElseThrow(() -> new CustomException("Especialidad inválida Id: " + idEsp));
			prof.setEspecialidad(espNueva);

			// Asociar Sucursales
			Set<Sucursal> sucursalesSet = sucursalesIds.stream()
					.map(id -> sucursalRepo.findById(id)
							.orElseThrow(() -> new CustomException("Sucursal inválida Id: " + id)))
					.collect(Collectors.toSet());
			prof.setSucursales(sucursalesSet);

			// Manejo de nuevas Disponibilidades
			List<Disponibilidad> listaDispDelFormulario = profForm.getDisponibilidades();
			if (listaDispDelFormulario != null) {
				// Validación antes de persistir
				String solapamientoMensaje = validarSinSolapamientos(listaDispDelFormulario);
				if (solapamientoMensaje != null) {
					model.addAttribute("errorDisp", solapamientoMensaje);
					model.addAttribute("profesionales", profesionalRepo.findAll());
					model.addAttribute("especialidadesList", especialidadRepo.findAll());
					model.addAttribute("sucursalesList", sucursalRepo.findAll());
					model.addAttribute("profesionalForm", profForm);
					return "empleado/profesionales";
				}

				// Si todo va ok, asigno cada Disponibilidad
				for (Disponibilidad dForm : listaDispDelFormulario) {
					Sucursal suc = sucursalRepo.findById(dForm.getSucursal().getIdSucursal())
							.orElseThrow(() -> new CustomException(
									"Sucursal inválida Id: " + dForm.getSucursal().getIdSucursal()));
					dForm.setSucursal(suc);
					dForm.setProfesional(prof);
					prof.getDisponibilidades().add(dForm);
				}
			}

			profesionalRepo.save(prof);
		}

		return "redirect:/empleado/profesionales";
	}

	// Recorre la lista de Disponibilidades y devuelve null si no existen
	// solapamientos
	// En caso de encontrar fecha y horarios iguales que se superponen, devuelve el
	// mensaje de error con detalle

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

					return String.format("Conflicto: para el día %s, el horario %s–%s\n   se superpone con %s–%s",
							d1.getFecha(), d1.getHoraInicio(), d1.getHoraFin(), d2.getHoraInicio(), d2.getHoraFin());
				}
			}
		}
		return null;
	}

	@GetMapping("/edit/{id}")
	public String editarProfesional(@PathVariable("id") Long id, Model model) {
		Profesional prof = profesionalRepo.findByIdWithDisponibilidades(id)
				.orElseThrow(() -> new CustomException("Profesional inválido Id: " + id));

		if (prof.getDisponibilidades() == null) {
			prof.setDisponibilidades(new ArrayList<>());
		}

		prof.getSucursales().size();

		model.addAttribute("profesionalForm", prof);

		List<Profesional> lista = profesionalRepo.findAll();
		model.addAttribute("profesionales", lista);

		List<Especialidad> especialidades = especialidadRepo.findAll();
		model.addAttribute("especialidadesList", especialidades);

		List<Sucursal> sucursales = sucursalRepo.findAll();
		model.addAttribute("sucursalesList", sucursales);

		return "empleado/profesionales";
	}

	// Eliminar un profesional: GET /delete/{id}

	@GetMapping("/delete/{id}")
	public String eliminarProfesional(@PathVariable("id") Long id) {
	    if (turnoRepo.existsByProfesional_IdUsuario(id)) throw new CustomException("El profesional posee Turnos asignados.");
		profesionalRepo.deleteById(id);
		return "redirect:/empleado/profesionales";
	}
}
