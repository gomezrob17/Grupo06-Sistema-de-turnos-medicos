Proyecto de Gestión de Turnos Médicos

Descripción

Sistema web para la gestión de turnos médicos que permite:

ABM (Alta, Baja y Modificación) de Sucursales, Especialidades y Profesionales.

Definición de Disponibilidades de profesionales por sucursal y horario.

Reserva de turnos por parte de pacientes.

Visualización de la agenda del paciente y control de estados de turno (PENDIENTE, CONFIRMADO, CANCELADO).

Seguridad con Spring Security y roles diferenciados (ROLE_EMPLEADO y ROLE_PACIENTE).

Funcionalidades principales

Autenticación y registro de usuarios (empleados y pacientes).

ABM de Sucursales.

ABM de Especialidades.

ABM de Profesionales (incluye asignación de especialidad, sucursales y disponibilidades).

Gestión de Disponibilidades de Profesionales.

Búsqueda de Horarios Disponibles vía API REST y AJAX.

Reserva de Turnos y gestión de estados.

Envió de Reserva de Turno via email a usuario Paciente

Panel de administración para empleados.

Configuración inicial

Crear la base de datos en MySQL manualmente: CREATE DATABASE turnos_medicos;

Editar credenciales en src/main/resources/application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/turnos_medicos
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_CONTRASEÑA

Inicializar tablas y datos

(1) Ejecutar la aplicación (Run/Spring) para que JPA genere las tablas.

(2) Detener la aplicación.

(3) En src/main/resources/data.sql, descomentar las sentencias INSERT.

(4) Reiniciar la aplicación para cargar roles, estados de turno y usuario admin.

(5) Comentar nuevamente data.sql para evitar duplicados en futuros arranques.

Usuarios de prueba

Administrador (Empleado)

Email: admin@vidanova.com

Contraseña: admin123

Paciente

En la pantalla de login, click en Registrarse. Completar datos y crear cuenta con email y contraseña. Iniciar sesión con esas credenciales.


Problemas conocidos / Pendientes

Eliminar Sucursal: falta implementar la entidad ProfesionalSucursal.java para manejar la relación y permitir la eliminación sin violaciones de integridad.

Eliminación de Reserva: pendiente por complicaciones con relaciones de entidades que impedia el normal funcionamiento de las cargas de reservas.

En formulario de registro de Paciente, falta el campo Obra Social (no afecta la integridad de nuestro sistema)
