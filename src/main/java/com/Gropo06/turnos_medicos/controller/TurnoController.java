package com.Gropo06.turnos_medicos.controller;

import com.Gropo06.turnos_medicos.dto.*;
import com.Gropo06.turnos_medicos.mapper.MapperUtil;
import com.Gropo06.turnos_medicos.model.Disponibilidad;
import com.Gropo06.turnos_medicos.model.Especialidad;
import com.Gropo06.turnos_medicos.model.Sucursal;
import com.Gropo06.turnos_medicos.model.Agenda;
import com.Gropo06.turnos_medicos.model.enums.DiaSemana;
import com.Gropo06.turnos_medicos.repository.AgendaRepository;
import com.Gropo06.turnos_medicos.repository.DisponibilidadRepository;
import com.Gropo06.turnos_medicos.repository.EstadoTurnoRepository;
import com.Gropo06.turnos_medicos.repository.ProfesionalRepository;
import com.Gropo06.turnos_medicos.service.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Controller
public class TurnoController {

	private final EspecialidadService especialidadService;
	private final SucursalService sucursalService;
	private final PacienteService pacienteService;
	private final EstadoTurnoService estadoTurnoService;
	private final TurnoService turnoService;
	private final DisponibilidadRepository dispRepo;
	private final AgendaRepository agendaRepo;
	private final ProfesionalRepository profesionalRepo;
	private final EstadoTurnoRepository estadoTurnoRepo;
	private final EmailService emailService;

	public TurnoController(EspecialidadService especialidadService, SucursalService sucursalService,
			PacienteService pacienteService, EstadoTurnoService estadoTurnoService, TurnoService turnoService,
			DisponibilidadRepository dispRepo, AgendaRepository agendaRepo, ProfesionalRepository profesionalRepo,
			EstadoTurnoRepository estadoTurnoRepo, EmailService emailService) {
		this.especialidadService = especialidadService;
		this.sucursalService = sucursalService;
		this.pacienteService = pacienteService;
		this.estadoTurnoService = estadoTurnoService;
		this.turnoService = turnoService;
		this.dispRepo = dispRepo;
		this.agendaRepo = agendaRepo;
		this.profesionalRepo = profesionalRepo;
		this.estadoTurnoRepo = estadoTurnoRepo;
		this.emailService = emailService;
	}

	@GetMapping("/api/turnos/slots")
	@ResponseBody
	public ResponseEntity<List<SlotDTO>> getSlotsPorFiltro(@RequestParam Long idEspecialidad,
			@RequestParam Long idSucursal, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

		EspecialidadDTO espDto = especialidadService.buscarEspecialidadPorId(idEspecialidad);
		SucursalDTO sucDto = sucursalService.findById(idSucursal);
		var espEnt = MapperUtil.toEntity(espDto);
		var sucEnt = MapperUtil.toEntity(sucDto);

		int duracionMin = espDto.getDuracion();
		List<Disponibilidad> listaDisp = dispRepo.findByProfesional_EspecialidadAndSucursal(espEnt, sucEnt);

		List<SlotDTO> resultado = new ArrayList<>();
		for (Disponibilidad disp : listaDisp) {
			var profEnt = disp.getProfesional();
			ProfesionalDTO profDto = MapperUtil.toDto(profEnt);
			DiaSemana dia = disp.getDiaSemana();
			List<LocalDate> fechas = fechasQueCoincidenConDiaSemana(desde, hasta, dia);

			for (LocalDate fecha : fechas) {
				Optional<Agenda> opAg = agendaRepo.findByDiaAndSucursalAndTipoEspecialidad(fecha, sucEnt, espEnt);
				Set<LocalTime> ocupadas = new HashSet<>();
				if (opAg.isPresent()) {
					opAg.get().getTurnos().stream()
							.filter(t -> t.getProfesional().getIdUsuario().equals(profEnt.getIdUsuario()))
							.map(t -> t.getHora()).forEach(ocupadas::add);
				}

				LocalTime cursor = disp.getHoraInicio();
				while (!cursor.plusMinutes(duracionMin).isAfter(disp.getHoraFin())) {
					LocalTime tope = cursor.plusMinutes(duracionMin);
					if (!ocupadas.contains(cursor)) {
						resultado.add(new SlotDTO(profDto.getIdUsuario(), espDto.getIdEspecialidad(),
								sucDto.getIdSucursal(), fecha, cursor, tope));
					}
					cursor = tope;
				}
			}
		}

		return ResponseEntity.ok(resultado);
	}

	@GetMapping("/turnos")
	public String mostrarFiltroInicial(Model model) {
		model.addAttribute("especialidades", especialidadService.getAll());
		model.addAttribute("sucursales", Collections.emptyList());
		model.addAttribute("filtro", new FiltroTurnoDTO());
		model.addAttribute("disponibilidadesPorProfesional", null);
		return "paciente/turnos";
	}

	@PostMapping("/turnos/buscar")
	public String buscarDisponibilidades(@ModelAttribute("filtro") FiltroTurnoDTO filtro, Model model) {

		model.addAttribute("especialidades", especialidadService.getAll());
		if (filtro.getIdEspecialidad() != null) {
			model.addAttribute("sucursales", sucursalService.findByEspecialidadId(filtro.getIdEspecialidad()));
		} else {
			model.addAttribute("sucursales", Collections.emptyList());
		}

		Map<ProfesionalDTO, Map<LocalDate, List<SlotDTO>>> mapSlots = new LinkedHashMap<>();

		if (filtro.getIdEspecialidad() != null && filtro.getIdSucursal() != null && filtro.getFechaDesde() != null
				&& filtro.getFechaHasta() != null) {

			List<Disponibilidad> disponibilidades = dispRepo.findByProfesional_EspecialidadAndSucursal(
					MapperUtil.toEntity(especialidadService.buscarEspecialidadPorId(filtro.getIdEspecialidad())),
					MapperUtil.toEntity(sucursalService.findById(filtro.getIdSucursal())));

			for (Disponibilidad disp : disponibilidades) {
				ProfesionalDTO profesional = MapperUtil.toDto(disp.getProfesional());
				Map<LocalDate, List<SlotDTO>> slotsPorFecha = mapSlots.computeIfAbsent(profesional,
						k -> new LinkedHashMap<>());

				List<LocalDate> fechas = fechasQueCoincidenConDiaSemana(filtro.getFechaDesde(), filtro.getFechaHasta(),
						disp.getDiaSemana());

				int duracion = especialidadService.buscarEspecialidadPorId(filtro.getIdEspecialidad()).getDuracion();
				for (LocalDate fecha : fechas) {
					List<SlotDTO> slots = slotsPorFecha.computeIfAbsent(fecha, k -> new ArrayList<>());
					LocalTime cursor = disp.getHoraInicio();
					while (!cursor.plusMinutes(duracion).isAfter(disp.getHoraFin())) {
						LocalTime fin = cursor.plusMinutes(duracion);
						slots.add(new SlotDTO(profesional.getIdUsuario(), filtro.getIdEspecialidad(),
								filtro.getIdSucursal(), fecha, cursor, fin));
						cursor = fin;
					}
				}
			}
		}

		model.addAttribute("disponibilidadesPorProfesional", mapSlots);
		return "paciente/turnos";
	}

	@PostMapping("/turnos/reservar")
	public String reservarTurno(@RequestParam Long idProfesional, @RequestParam Long idEspecialidad,
			@RequestParam Long idSucursal, @RequestParam String fecha, @RequestParam String horaInicio,
			Principal principal, RedirectAttributes flash) {
		try {
			System.out.println("=== INICIANDO RESERVA DE TURNO ===");

			PacienteDTO paciente = pacienteService.findByEmail(principal.getName());

			EspecialidadDTO espDTO = especialidadService.buscarEspecialidadPorId(idEspecialidad);
			SucursalDTO sucDTO = sucursalService.findById(idSucursal);

			Especialidad esp = MapperUtil.toEntity(espDTO);
			Sucursal suc = MapperUtil.toEntity(sucDTO);

			LocalDate fechaTurno = LocalDate.parse(fecha);

			System.out.println("Buscando agenda para: fecha=" + fecha + ", sucursal=" + idSucursal + ", especialidad="
					+ idEspecialidad);

			Agenda agenda;

			// Creamos la Agenda si no existe
			Optional<Agenda> agendaOpt = agendaRepo.findByDiaAndSucursalAndTipoEspecialidad(fechaTurno, suc, esp);
			if (agendaOpt.isEmpty()) {
				agenda = new Agenda();
				agenda.setDia(fechaTurno);
				agenda.setTipoEspecialidad(esp);
				agenda.setSucursal(suc);
				agenda = agendaRepo.save(agenda); // GUARDA Y OBTIENE ID

				agenda = agendaRepo.findById(agenda.getIdAgenda()).orElseThrow();
				System.out.println("AGENDA CREADA automáticamente: " + agenda.getIdAgenda());
				flash.addFlashAttribute("infoMsg", "Se creó una agenda nueva para la fecha seleccionada.");
			} else {
				agenda = agendaOpt.get();
			}

			// Antes de guardar, verificamos si ya existe turno igual
			LocalTime hora = LocalTime.parse(horaInicio);
			boolean yaExiste = agenda.getTurnos().stream()
					.anyMatch(t -> t.getProfesional().getIdUsuario().equals(idProfesional)
							&& t.getPaciente().getIdUsuario().equals(paciente.getIdUsuario())
							&& t.getHora().equals(hora));
			if (yaExiste) {
				flash.addFlashAttribute("warningMsg", "Ya existe un turno reservado para esa fecha y hora.");
				return "redirect:/mis-turnos";
			}

			// Guardamos el turno
			TurnoDTO nuevo = new TurnoDTO();
			nuevo.setProfesionalId(idProfesional);
			nuevo.setPacienteId(paciente.getIdUsuario());
			nuevo.setEstadoId(estadoTurnoService.obtenerPorNombre("PENDIENTE").getIdEstado());
			nuevo.setHora(hora);
			nuevo.setAgendaId(agenda.getIdAgenda());

			turnoService.crearTurno(nuevo);

			emailService.enviarCorreo(paciente.getContactoEmail(), "Confirmación de Turno",
					"Su turno quedó reservado para el " + fecha + " a las " + horaInicio);

			flash.addFlashAttribute("successMsg", "Turno reservado correctamente");
			return "redirect:/mis-turnos";

		} catch (Exception e) {
			flash.addFlashAttribute("errorMsg", "Error al reservar: " + e.getMessage());
			return "redirect:/mis-turnos";
		}
	}

	// Muestramos los TurnoDTO del paciente logueado
	@GetMapping("/mis-turnos")
	public String verMisTurnos(Model model, Principal principal) {
		PacienteDTO paciente = pacienteService.findByEmail(principal.getName());
		List<TurnoDTO> turnos = turnoService.findByPacienteId(paciente.getIdUsuario());
		List<TurnoVistaDTO> turnosVista = new ArrayList<>();

		for (TurnoDTO turno : turnos) {
			TurnoVistaDTO det = new TurnoVistaDTO();
			det.setIdTurno(turno.getIdTurno());
			det.setHora(turno.getHora());
			det.setAgenda(agendaRepo.findById(turno.getAgendaId()).orElse(null));
			det.setProfesional(profesionalRepo.findById(turno.getProfesionalId()).orElse(null));
			det.setEstado(estadoTurnoRepo.findById(turno.getEstadoId()).orElse(null));

			turnosVista.add(det);
		}

		model.addAttribute("misTurnos", turnosVista);
		return "paciente/mis_turnos";
	}

	// Calculamos fechas por día de semana
	private List<LocalDate> fechasQueCoincidenConDiaSemana(LocalDate desde, LocalDate hasta, DiaSemana diaBuscado) {
		List<LocalDate> resultado = new ArrayList<>();
		LocalDate actual = desde;
		while (!actual.isAfter(hasta)) {
			if (DiaSemana.desde(actual) == diaBuscado) {
				resultado.add(actual);
			}
			actual = actual.plusDays(1);
		}
		return resultado;
	}
}
