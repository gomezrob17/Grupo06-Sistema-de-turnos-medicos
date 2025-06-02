
/*INSERT INTO roles(nombre) VALUES ('EMPLEADO'), ('PACIENTE');*/

-- 1) Creo el contacto del EMPLEADO "Admin"
/*INSERT INTO contactos (email, telefono)
VALUES ('admin@vidanova.com', '111-222-3333');

-- 2) Creo el usuario con ese mismo contacto y asigno rol=1 (EMPLEADO)
INSERT INTO usuarios
  (dni, nombre, apellido, fecha_nacimiento, genero, id_contacto, id_rol, password)
VALUES
  (
    '00000000',
    'Admin',
    'Super',
    '1990-01-01',
    'Masculino',
    (SELECT id_contacto FROM contactos WHERE email = 'admin@vidanova.com'),
    1,
    '$2a$10$3HE8HDJBWENBaGp6KNEbKekqnPy71w5XiIzmhO6HeLCraGrDsD/q.'
  );

-- 3) Genero el registro en empleados señalando que está activo
INSERT INTO empleados (id_usuario, activo)
VALUES (
  (SELECT id_usuario FROM usuarios WHERE dni = '00000000'),
  TRUE
);*/
