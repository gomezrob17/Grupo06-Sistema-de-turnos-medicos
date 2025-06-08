package com.Gropo06.turnos_medicos.controller;

import com.Gropo06.turnos_medicos.dto.FiltroTurnoDTO;
import com.Gropo06.turnos_medicos.dto.SlotDTO;
import com.Gropo06.turnos_medicos.exceptions.CustomException;
import com.Gropo06.turnos_medicos.model.Agenda;
import com.Gropo06.turnos_medicos.model.Disponibilidad;
import com.Gropo06.turnos_medicos.model.Especialidad;
import com.Gropo06.turnos_medicos.model.EstadoTurno;
import com.Gropo06.turnos_medicos.model.Paciente;
import com.Gropo06.turnos_medicos.model.Profesional;
import com.Gropo06.turnos_medicos.model.Sucursal;
import com.Gropo06.turnos_medicos.model.Turno;
import com.Gropo06.turnos_medicos.repository.AgendaRepository;
import com.Gropo06.turnos_medicos.repository.DisponibilidadRepository;
import com.Gropo06.turnos_medicos.repository.EspecialidadRepository;
import com.Gropo06.turnos_medicos.repository.EstadoTurnoRepository;
import com.Gropo06.turnos_medicos.repository.PacienteRepository;
import com.Gropo06.turnos_medicos.repository.ProfesionalRepository;
import com.Gropo06.turnos_medicos.repository.SucursalRepository;
import com.Gropo06.turnos_medicos.repository.TurnoRepository;
import com.Gropo06.turnos_medicos.service.EmailService;
import com.Gropo06.turnos_medicos.service.TurnoService;

import org.springframework.beans.factory.annotation.Autowired;
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

    private final EspecialidadRepository espRepo;
    private final SucursalRepository sucRepo;
    private final PacienteRepository pacienteRepo;
    private final DisponibilidadRepository dispRepo;
    private final TurnoRepository turnoRepo;
    private final AgendaRepository agendaRepo;
    private final ProfesionalRepository profRepo;
    private final EstadoTurnoRepository estadoRepo;
    @Autowired
    private EmailService emailService;
    @Autowired
    private TurnoService turnoService;
    

    public TurnoController(EspecialidadRepository espRepo,
                           SucursalRepository sucRepo,
                           PacienteRepository pacienteRepo,
                           DisponibilidadRepository dispRepo,
                           TurnoRepository turnoRepo,
                           AgendaRepository agendaRepo,
                           ProfesionalRepository profRepo,
                           EstadoTurnoRepository estadoRepo) {
        this.espRepo       = espRepo;
        this.sucRepo       = sucRepo;
        this.pacienteRepo  = pacienteRepo;
        this.dispRepo      = dispRepo;
        this.turnoRepo     = turnoRepo;
        this.agendaRepo    = agendaRepo;
        this.profRepo      = profRepo;
        this.estadoRepo    = estadoRepo;
    }


    // DTO interno para serializar sucursales en JSON
    public static class SucursalDTO {
        private Long id;
        private String nombre;

        public SucursalDTO(Long id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }
        public Long getId()       { return id; }
        public String getNombre() { return nombre; }
    }

    @GetMapping("/api/turnos/slots")
    @ResponseBody
    public ResponseEntity<List<SlotDTO>> getSlotsPorFiltro(
            @RequestParam("idEspecialidad") Long idEsp,
            @RequestParam("idSucursal") Long idSuc,
            @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

        // 1) Valido y recupero Especialidad y Sucursal
        Especialidad espElegida = espRepo.findById(idEsp)
                .orElseThrow(() -> new CustomException("Especialidad inválida Id: " + idEsp));
        Sucursal sucElegida = sucRepo.findById(idSuc)
                .orElseThrow(() -> new CustomException("Sucursal inválida Id: " + idSuc));

        // 2) Busco en Disponibilidad todas las filas que coincidan:
        //    - Profesional con espElegida
        //    - Sucursal = sucElegida
        //    - Fecha entre [desde .. hasta]
        List<Disponibilidad> listaDisp = dispRepo
            .findByProfesional_EspecialidadAndSucursalAndFechaBetween(
                espElegida,
                sucElegida,
                desde,
                hasta
            );

        int duracionMinutos = espElegida.getDuracion();
        List<SlotDTO> resultado = new ArrayList<>();

        // 3) Por cada Disponibilidad genero sub-intervalos de "duracionMinutos" sin colisión con Turnos ya reservados
        for (Disponibilidad disp : listaDisp) {
            Profesional prof = disp.getProfesional();
            LocalDate fechaDispon = disp.getFecha();
            LocalTime inicio = disp.getHoraInicio();
            LocalTime fin    = disp.getHoraFin();

            // 3.a) Recupero (si existe) la Agenda para esta combinación (fecha, sucursal, especialidad)
            Optional<Agenda> opAg = agendaRepo
                    .findByDiaAndSucursalAndTipoEspecialidad(fechaDispon, sucElegida, espElegida);

            // 3.b) Recojo todas las horas ya ocupadas (turnos ya guardados) en esta agenda para este profesional
            Set<LocalTime> horasOcupadas = new HashSet<>();
            if (opAg.isPresent()) {
                Agenda agenda = opAg.get();
                for (Turno t : agenda.getTurnos()) {
                    if (t.getProfesional().getIdUsuario().equals(prof.getIdUsuario())) {
                        horasOcupadas.add(t.getHora());
                    }
                }
            }

            // 3.c) Recorro desde "inicio" en incrementos de "duracionMinutos" hasta "fin"
            LocalTime cursor = inicio;
            while (!cursor.plusMinutes(duracionMinutos).isAfter(fin)) {
                LocalTime tope = cursor.plusMinutes(duracionMinutos);
                // Solo añado si "cursor" no está ocupado (no colisiona con un Turno ya existente)
                if (!horasOcupadas.contains(cursor)) {
                    SlotDTO slot = new SlotDTO(
                        prof.getIdUsuario(),
                        espElegida.getIdEspecialidad(),
                        sucElegida.getIdSucursal(),
                        fechaDispon,
                        cursor,
                        tope
                    );
                    resultado.add(slot);
                }
                cursor = tope;
            }
        }

        return ResponseEntity.ok(resultado);
    }

    //======================================================================================================
    // 3) VISTA Thymeleaf: GET /turnos
    //    Muestra el filtro inicial (especialidad + sucursal + fechas). Todavía no hay slots.
    //======================================================================================================
    @GetMapping("/turnos")
    public String mostrarFiltroInicial(Model model, Principal principal) {
        // 1) Cargo todas las especialidades para el <select>
        List<Especialidad> especialidades = espRepo.findAll();
        model.addAttribute("especialidades", especialidades);

        // 2) Inicialmente no hay sucursales (se llenarán dinámicamente)
        model.addAttribute("sucursales", List.of());

        // 3) Nuevo DTO vacío para bindear el formulario
        model.addAttribute("filtro", new FiltroTurnoDTO());

        // 4) Aún no calculamos slots ni los mostramos
        model.addAttribute("disponibilidadesPorProfesional", null);

        return "paciente/turnos";
    }

    //======================================================================================================
    // 4) VISTA Thymeleaf: POST /turnos/buscar
    //    Procesa un formulario clásico (sin AJAX) y devuelve la misma página con "disponibilidadesPorProfesional" en el modelo
    //======================================================================================================
    @PostMapping("/turnos/buscar")
    public String buscarDisponibilidades(
            @ModelAttribute("filtro") FiltroTurnoDTO filtro,
            Model model
    ) {
        // 1) Recargo la lista de especialidades para que el <select> siga disponible
        List<Especialidad> especialidades = espRepo.findAll();
        model.addAttribute("especialidades", especialidades);

        // 2) Si ya se eligió especialidad, recupero sucursales correspondientes
        if (filtro.getIdEspecialidad() != null) {
            Especialidad espElegida = espRepo.findById(filtro.getIdEspecialidad())
                    .orElseThrow(() -> new CustomException("Especialidad inválida"));
            List<Sucursal> sucursalesQueAtienden = sucRepo.findDistinctByProfesionalesEspecialidadId(filtro.getIdEspecialidad());
            model.addAttribute("sucursales", sucursalesQueAtienden);
        } else {
            model.addAttribute("sucursales", List.of());
        }

        // 3) Si falta algún dato obligatorio, regreso la misma vista sin calcular nada
        if (filtro.getIdEspecialidad() == null
                || filtro.getIdSucursal() == null
                || filtro.getFechaDesde() == null
                || filtro.getFechaHasta() == null) {
            model.addAttribute("disponibilidadesPorProfesional", null);
            return "paciente/turnos";
        }

        // 4) Recupero entidades completas
        Especialidad espElegida = espRepo.findById(filtro.getIdEspecialidad())
                .orElseThrow(() -> new CustomException("Especialidad inválida"));
        Sucursal sucElegida = sucRepo.findById(filtro.getIdSucursal())
                .orElseThrow(() -> new CustomException("Sucursal inválida"));

        LocalDate desde = filtro.getFechaDesde();
        LocalDate hasta = filtro.getFechaHasta();

        // 5) Traigo todas las Disponibilidades que cumplen los criterios
        List<Disponibilidad> listaDisp = dispRepo
                .findByProfesional_EspecialidadAndSucursalAndFechaBetween(
                        espElegida,
                        sucElegida,
                        desde,
                        hasta
                );
        

        int duracionMinutos = espElegida.getDuracion();
        Map<Profesional, List<SlotDTO>> mapSlots = new LinkedHashMap<>();

        // 6) Por cada Disponibilidad, genero los SlotDTO libres
        for (Disponibilidad disp : listaDisp) {
            Profesional prof = disp.getProfesional();
            LocalDate fechaDispon = disp.getFecha();
            LocalTime inicio = disp.getHoraInicio();
            LocalTime fin = disp.getHoraFin();

            // 6.a) Recupero (si existe) la Agenda del día+especialidad+sucursal
            Optional<Agenda> opAg = agendaRepo.findByDiaAndSucursalAndTipoEspecialidad(
                    fechaDispon, sucElegida, espElegida
            );

            // 6.b) Recojo horas ocupadas por turnos ya guardados
            Set<LocalTime> horasOcupadas = new HashSet<>();
            if (opAg.isPresent()) {
                Agenda agenda = opAg.get();
                for (Turno t : agenda.getTurnos()) {
                    if (t.getProfesional().getIdUsuario().equals(prof.getIdUsuario())) {
                        horasOcupadas.add(t.getHora());
                    }
                }
            }

            // 6.c) Genero sub-intervalos de "duracionMinutos"
            LocalTime cursor = inicio;
            List<SlotDTO> slotsDeEsteDisp = new ArrayList<>();
            while (!cursor.plusMinutes(duracionMinutos).isAfter(fin)) {
                LocalTime tope = cursor.plusMinutes(duracionMinutos);
                if (!horasOcupadas.contains(cursor)) {
                    SlotDTO slot = new SlotDTO(
                            prof.getIdUsuario(),
                            espElegida.getIdEspecialidad(),
                            sucElegida.getIdSucursal(),
                            fechaDispon,
                            cursor,
                            tope
                    );
                    slotsDeEsteDisp.add(slot);
                }
                cursor = tope;
            }

            // 6.d) Agrego a la colección global, agrupando por Profesional
            mapSlots.computeIfAbsent(prof, k -> new ArrayList<>())
                    .addAll(slotsDeEsteDisp);
        }

        // 7) Paso el map a Thymeleaf (la vista iterará sobre el mapa y mostrará botones para cada SlotDTO)
        model.addAttribute("disponibilidadesPorProfesional", mapSlots);

        return "paciente/turnos";
    }

    //======================================================================================================
    // 5) VISTA Thymeleaf: POST /turnos/reservar
    //    Reserva un turno y redirige a /mis-turnos
    //======================================================================================================
    @PostMapping("/turnos/reservar")
    public String reservarTurno(
            @RequestParam("idProfesional") Long idProf,
            @RequestParam("idEspecialidad") Long idEsp,
            @RequestParam("idSucursal") Long idSuc,
            @RequestParam("fecha") String fechaStr,      // ej. "2025-06-05"
            @RequestParam("horaInicio") String horaInicioStr, // ej. "09:00"
            Principal principal,
            RedirectAttributes flash
    ) {
        try {
            // 1) Recupero el paciente logueado por email (contacto.email)
            String emailUsuario = principal.getName();
            Paciente paciente = pacienteRepo.findByContactoEmail(emailUsuario)
                    .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));

            // 2) Convierto Strings a LocalDate / LocalTime
            LocalDate fecha = LocalDate.parse(fechaStr);
            LocalTime horaInicio = LocalTime.parse(horaInicioStr);

            // 3) Busco Profesional, Especialidad y Sucursal
            Profesional profesional = profRepo.findById(idProf)
                    .orElseThrow(() -> new IllegalArgumentException("Profesional inválido"));
            Especialidad especialidad = espRepo.findById(idEsp)
                    .orElseThrow(() -> new IllegalArgumentException("Especialidad inválida"));
            Sucursal sucursal = sucRepo.findById(idSuc)
                    .orElseThrow(() -> new IllegalArgumentException("Sucursal inválida"));

            // 4) Obtengo (o creo) la Agenda para (fecha, sucursal, especialidad)
            Optional<Agenda> opAg = agendaRepo.findByDiaAndSucursalAndTipoEspecialidad(fecha, sucursal, especialidad);
            Agenda agenda = opAg.orElseGet(() -> {
                Agenda nueva = new Agenda();
                nueva.setDia(fecha);
                nueva.setTipoEspecialidad(especialidad);
                nueva.setSucursal(sucursal);
                agendaRepo.save(nueva);
                return nueva;
            });

            // 5) Verifico que NO exista un turno con ese profesional y hora en esta agenda
            boolean yaReservado = agenda.getTurnos().stream()
                    .anyMatch(t ->
                            t.getProfesional().getIdUsuario().equals(idProf)
                                    && t.getHora().equals(horaInicio)
                    );
            if (yaReservado) {
                flash.addFlashAttribute("errorMsg",
                        "Lo sentimos, esa franja se acaba de reservar. Intenta con otro horario.");
                return "redirect:/turnos";
            }

            // 6) Creo y guardo el turno
            Turno nuevoTurno = new Turno();
            nuevoTurno.setHora(horaInicio);
            nuevoTurno.setPaciente(paciente);
            nuevoTurno.setProfesional(profesional);

            EstadoTurno estadoPendiente = estadoRepo.findById(1L)
                    .orElseThrow(() -> new IllegalArgumentException("EstadoTurno no encontrado"));
            nuevoTurno.setEstado(estadoPendiente);

            nuevoTurno.setAgenda(agenda);
            turnoRepo.save(nuevoTurno);

            // 7) ENVÍO DE CORREO con emailService y datos desde paciente.getContacto().getEmail()
            String emailDestino = paciente.getContacto().getEmail();
            String asunto = "Confirmación de Turno Médico";
            String texto = String.format(
                    "Hola %s,\n\nSu turno con el profesional %s para la especialidad %s\n" +
                            "en la sucursal %s ha sido reservado para el día %s a las %s.\n\nGracias por confiar en nosotros.",
                    paciente.getNombre(),
                    profesional.getNombre(),
                    especialidad.getNombre(),
                    sucursal.getNombre(),
                    fecha,
                    horaInicio
            );
            emailService.enviarCorreo(emailDestino, asunto, texto);

            flash.addFlashAttribute("successMsg",
                    "¡Turno reservado correctamente para el " + fecha + " a las " + horaInicio + "!");
            return "redirect:/mis-turnos";

        } catch (Exception e) {
            flash.addFlashAttribute("errorMsg", "Error al reservar turno: " + e.getMessage());
            return "redirect:/mis-turnos";
        }
    }

    //======================================================================================================
    // 6) VISTA Thymeleaf: GET /mis-turnos
    //    Muestra la lista de turnos que el paciente ya tiene agendados.
    //======================================================================================================
    @GetMapping("/mis-turnos")
    public String verMisTurnos(Model model, Principal principal) {
        // 1) Recupero el paciente logueado
        String email = principal.getName();
        Paciente paciente = pacienteRepo
                .findByContactoEmail(email)
                .orElseThrow(() -> new CustomException("Paciente no encontrado"));

        // 2) Busco todos los turnos de ese paciente
        List<Turno> misTurnos = turnoRepo.findByPaciente(paciente);

        // 3) Paso al modelo para la vista
        model.addAttribute("misTurnos", misTurnos);
        return "paciente/mis_turnos";
    }
    
    @PostMapping("/turnos/cancelar")
    public String cancelarTurno(@RequestParam("idTurno") Long idTurno) {
        Turno turno = turnoRepo.findById(idTurno)
                .orElseThrow(() -> new CustomException("Turno no encontrado"));

            if (turno.getEstado().getNombre().equalsIgnoreCase("Cancelado")) {
                throw new CustomException("El turno ya está cancelado");
            }
        turnoService.cancelarTurno(idTurno);
        return "redirect:/mis-turnos";
    }
}
