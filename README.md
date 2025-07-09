## Proyecto para la materia Orientación a Objetos 2

Proyecto: Gestión de Turnos Médicos.
- - -
- Versión de Java 17 o superior.
- Maven 4 o superior.
- Lombok instalado en el IDE.s
- MySQL como Base de Datos.
- Crear la base de datos en MySQL manualmente: CREATE DATABASE `turnos_medicos`;
- Abrir el proyecto y revisar que estén las dependencias descargadas, sino abrir una terminal en la raíz del proyecto y ejecutar esta instrucción: mvn clean install
- Configurar las variables de entorno para que el archivo application.properties las reconozca antes de iniciar la aplicación, haciendo clic derecho en el proyecto -> Run as  -> Run Configurations -> Environment:
  + DB_URL -> colocar la url de la base de datos.
  + DB_USERNAME -> colocar su usuario de la base de datos.
  + DB_PASSWORD -> colocar tu contraseña de la base de datos.
  + EMAIL_USERNAME -> colocar mail habilitado para envio
  + EMAIL_PASSWORD -> colocar contraseña de aplicacion del mail
- Inicializar tablas y datos
  + (1) Ejecutar la aplicación (Run/Spring) para que JPA genere las tablas.
  + (2) Detener la aplicación.
  + (3) En src/main/resources/data.sql, descomentar las sentencias INSERT.
  + (4) Reiniciar la aplicación para cargar roles, estados de turno y usuario admin.
  + (5) Comentar nuevamente data.sql para evitar duplicados en futuros arranques.
- Ejecutar el proyecto.
- Si todo esta correcto aparece que la aplicación inicio en el puerto correspondiente (:8080).
- Abrir el navegador e ir a la siguiente url: localhost:8080
- Para una demo con Admin:
    - Usuario: admin@vidanova.com
    - Contraseña: admin123
- Para una demo con Paciente es necesario registrarse en el sistema.
