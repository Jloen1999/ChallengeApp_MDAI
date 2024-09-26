Índice

# Planificación y análisis

## **Objetivo del Proyecto:**

El objetivo de esta aplicación web es proporcionar una plataforma donde los usuarios puedan crear, unirse y seguir su progreso en **retos de hábitos** comunitarios. Cada usuario puede participar en diferentes desafíos personales (ejercicio, lectura, meditación, etc.), ver su progreso y el de otros usuarios, y compartir sus avances dentro de una comunidad de soporte. La idea es fomentar la motivación y la constancia a través de la competencia amistosa y la colaboración social.

---

## **Funcionalidades Principales de la Aplicación:**

1. **Registro e inicio de sesión de usuarios**:
    - Los usuarios podrán registrarse con un correo y contraseña.
    - Autenticación y autorización con Spring Security.
    - Permitir que los usuarios inicien sesión y gestionen su perfil.
2. **Creación y gestión de retos**:
    - Los usuarios podrán crear retos personalizados, especificando el objetivo, la duración y la periodicidad (diaria, semanal).
    - Se podrán definir retos públicos (disponibles para todos) o privados (solo accesibles para amigos o invitados).
3. **Unirse a retos**:
    - Los usuarios podrán buscar y unirse a retos públicos.
    - En los retos privados, los usuarios recibirán una invitación del creador.
4. **Seguimiento del progreso**: 
    - Los usuarios podrán registrar su progreso (completar días del reto) e ingresar actualizaciones diarias.
    - Visualización gráfica de su avance y del avance grupal de todos los participantes del reto.
5. **Comentarios y soporte social**:
    - Cada reto tendrá una sección de comentarios para que los usuarios puedan interactuar, motivarse y compartir consejos.
6. **Historial de retos completados**:
    - Los usuarios podrán ver su historial de retos completados en su perfil, con las recompensas (insignias) obtenidas.
7. **Sistema de recompensas**:
    - Los usuarios ganarán insignias y reconocimientos al completar retos, visibles en su perfil.

---

### **Diagrama de Caso de Uso:**

```
+---------------------------------------------+
|             Diagrama de Caso de Uso         |
+---------------------------------------------+
|                 Usuario                     |
+---------------------------------------------+
| - Registrarse                               |
| - Iniciar Sesión                            |
| - Ver lista de retos                        |
| - Unirse a un reto                          |
| - Crear un reto                             |
| - Registrar progreso en un reto             |
| - Ver su progreso y el de otros             |
| - Comentar en un reto                       |
| - Ver perfil con historial de retos         |
+---------------------------------------------+

+---------------------------------------------+
|                 Administrador               |
+---------------------------------------------+
| - Gestionar usuarios (bloquear/eliminar)    |
| - Eliminar retos inapropiados               |
+---------------------------------------------+

```

### **Explicación de Casos de Uso:**

1. **Registrarse**: El usuario crea una cuenta proporcionando un correo y una contraseña.
2. **Iniciar sesión**: El usuario accede al sistema con sus credenciales.
3. **Ver lista de retos**: El usuario puede ver todos los retos públicos disponibles, filtrando por categoría (ejercicio, lectura, etc.).
4. **Unirse a un reto**: El usuario se une a un reto y comienza a registrar su progreso.
5. **Crear un reto**: El usuario define un nuevo reto, seleccionando el objetivo, la duración y si es público o privado.
6. **Registrar progreso en un reto**: El usuario actualiza su progreso en el reto al completar cada actividad diaria/semanal.
7. **Ver su progreso y el de otros**: El usuario puede ver visualmente su progreso y el de otros participantes en el reto.
8. **Comentar en un reto**: El usuario puede participar en discusiones dentro de un reto.
9. **Ver perfil con historial de retos**: El usuario revisa sus retos completados, los retos activos y las insignias obtenidas.

---

### **Diagrama Entidad-Relación (ER)**:

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/68661776-795c-4e20-885a-94a336b99b9f/dcec1e41-f741-4582-8f8d-4f693822ca05/image.png)

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/68661776-795c-4e20-885a-94a336b99b9f/e0df4379-62f3-47ed-a607-068a57286c97/image.png)

[Diagrama ER](https://www.notion.so/Diagrama-ER-2626710c1c1d44559c857fca27203ebe?pvs=21)

### **Entidades y Relaciones:**

1. **Usuario**:
    - Los usuarios pueden unirse a múltiples retos y pueden crear retos.
    - Cada usuario tiene un perfil con un nombre, correo y contraseña.
2. **Reto**:
    - Un reto puede tener múltiples participantes y comentarios.
    - Los retos tienen un nombre, descripción, duración y tipo (público o privado).
3. **Progreso_Reto**:
    - Esta tabla relaciona usuarios y retos para hacer un seguimiento del progreso de cada usuario en cada reto.
    - Cada entrada contiene el usuario, el reto, el progreso actual y la fecha de la última actualización.
4. **Comentario**:
    - Los comentarios se asocian con un reto y un usuario, permitiendo que los participantes interactúen.
    - Cada comentario incluye el texto y la fecha de publicación.

---

# Desarrollo

## Base de Datos

```sql
-- Creación de la base de datos
CREATE DATABASE IF NOT EXISTS retos_db;
USE retos_db;

-- Creación de la tabla Usuario
CREATE TABLE Usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL UNIQUE,
    contraseña VARCHAR(100) NOT NULL,
    perfil_info TEXT,
    ubicacion VARCHAR(255),
    CONSTRAINT UC_Usuario UNIQUE (correo)
);

-- Creación de la tabla Reto
CREATE TABLE Reto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT NOT NULL,
    duracion INT NOT NULL,
    estado INT NOT NULL,
    fecha_creacion DATE NOT NULL,
    fecha_finalizacion DATE NOT NULL,
    porcentaje_progreso DOUBLE,
    creador_id BIGINT,
    FOREIGN KEY (creador_id) REFERENCES Usuario(id)
);

-- Creación de la tabla RetoSimple (hereda de Reto)
CREATE TABLE RetoSimple (
    id BIGINT PRIMARY KEY,
    progreso JSON, -- Almacena un array de progreso en formato JSON
    FOREIGN KEY (id) REFERENCES Reto(id)
);

-- Creación de la tabla RetoComplejo (hereda de Reto)
CREATE TABLE RetoComplejo (
    id BIGINT PRIMARY KEY,
    subtarea_actual BIGINT, -- ID de la subtarea actual
    FOREIGN KEY (id) REFERENCES Reto(id)
);

-- Creación de la tabla Subtarea (relacionada con RetoComplejo)
CREATE TABLE Subtarea (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(255) NOT NULL,
    estado ENUM('PENDIENTE', 'COMPLETADA') NOT NULL,
    fecha_creacion DATE NOT NULL,
    fecha_finalizada DATE,
    reto_complejo_id BIGINT,
    FOREIGN KEY (reto_complejo_id) REFERENCES RetoComplejo(id)
);

-- Creación de la tabla ProgresoReto (relacionada con Usuario y Reto)
CREATE TABLE ProgresoReto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    progreso_actual DOUBLE NOT NULL,
    fecha_actualizacion DATE NOT NULL,
    usuario_id BIGINT,
    reto_id BIGINT,
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id),
    FOREIGN KEY (reto_id) REFERENCES Reto(id)
);

-- Creación de la tabla Recompensa (relacionada con Usuario y Reto)
CREATE TABLE Recompensa (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(255) NOT NULL,
    tipo VARCHAR(100) NOT NULL,
    puntos INT NOT NULL,
    usuario_id BIGINT,
    reto_id BIGINT,
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id),
    FOREIGN KEY (reto_id) REFERENCES Reto(id)
);

-- Creación de la tabla Estadistica (relacionada con Usuario)
CREATE TABLE Estadistica (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    total_retos INT NOT NULL,
    retos_completados INT NOT NULL,
    retos_fallidos INT NOT NULL,
    progreso_promedio DOUBLE NOT NULL,
    tiempo_promedio DOUBLE NOT NULL,
    usuario_id BIGINT,
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id)
);

-- Creación de la tabla Notificacion (relacionada con Usuario y Reto)
CREATE TABLE Notificacion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mensaje TEXT NOT NULL,
    leido BOOLEAN DEFAULT FALSE,
    fecha_envio DATE NOT NULL,
    usuario_id BIGINT,
    reto_id BIGINT,
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id),
    FOREIGN KEY (reto_id) REFERENCES Reto(id)
);

-- Creación de la tabla Comentario (relacionada con Usuario y Reto)
CREATE TABLE Comentario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    texto TEXT NOT NULL,
    fecha DATE NOT NULL,
    usuario_id BIGINT,
    reto_id BIGINT,
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id),
    FOREIGN KEY (reto_id) REFERENCES Reto(id)
);

-- Creación de la tabla de relaciones de amigos entre usuarios
CREATE TABLE Amigos (
    usuario_id BIGINT,
    amigo_id BIGINT,
    PRIMARY KEY (usuario_id, amigo_id),
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id),
    FOREIGN KEY (amigo_id) REFERENCES Usuario(id)
);

-- Creación de la tabla de participantes en retos
CREATE TABLE ParticipantesReto (
    reto_id BIGINT,
    usuario_id BIGINT,
    PRIMARY KEY (reto_id, usuario_id),
    FOREIGN KEY (reto_id) REFERENCES Reto(id),
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id)
);

-- Creación de la tabla SubtareaParticipante (relaciona Subtarea con Usuario)
CREATE TABLE SubtareaParticipante (
    subtarea_id BIGINT,
    usuario_id BIGINT,
    PRIMARY KEY (subtarea_id, usuario_id),
    FOREIGN KEY (subtarea_id) REFERENCES Subtarea(id),
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id)
);

```

```sql
INSERT INTO Usuario (nombre, correo, contraseña, perfil_info, ubicacion) VALUES
('Juan Pérez', 'juan.perez@example.com', 'password123', 'Amante de los retos físicos y mentales', 'Madrid'),
('Ana Gómez', 'ana.gomez@example.com', 'password456', 'Interesada en libros y retos creativos', 'Barcelona'),
('Luis Fernández', 'luis.fernandez@example.com', 'password789', 'Entusiasta del deporte', 'Valencia'),
('María Torres', 'maria.torres@example.com', 'password101', 'Aficionada a los desafíos culinarios', 'Sevilla'),
('Pedro García', 'pedro.garcia@example.com', 'password202', 'Fanático de los viajes y la aventura', 'Bilbao');

INSERT INTO Reto (nombre, descripcion, duracion, estado, fecha_creacion, fecha_finalizacion, porcentaje_progreso, creador_id) VALUES
('Leer 5 libros en un mes', 'Desafío literario para completar 5 libros en un mes', 30, 1, '2024-01-01', '2024-01-31', 0.0, 2),
('Correr 100 km en 10 días', 'Desafío de resistencia física para correr 100 km en total', 10, 2, '2024-02-01', '2024-02-10', 0.0, 1),
('Cocinar 10 recetas nuevas', 'Desafío de creatividad en la cocina', 15, 1, '2024-01-15', '2024-01-30', 0.0, 4),
('Explorar 5 nuevas ciudades', 'Desafío de aventura y viajes para conocer 5 nuevas ciudades', 60, 1, '2024-03-01', '2024-04-30', 0.0, 5),
('Participar en 3 carreras de 5 km', 'Desafío físico para completar 3 carreras de 5 km cada una', 20, 1, '2024-01-10', '2024-01-30', 0.0, 3);

INSERT INTO RetoSimple (id, progreso) VALUES
(1, JSON_ARRAY(0, 0, 0, 0, 0)), -- Progreso por libros en el reto de lectura
(2, JSON_ARRAY(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)), -- Progreso por cada km
(3, JSON_ARRAY(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)), -- Progreso por receta
(4, JSON_ARRAY(0, 0, 0, 0, 0)), -- Progreso por cada ciudad
(5, JSON_ARRAY(0, 0, 0)); -- Progreso por cada carrera

INSERT INTO RetoComplejo (id, subtarea_actual) VALUES
(6, NULL), -- Un reto complejo aún no iniciado
(7, NULL), -- Otro reto complejo aún no iniciado
(8, NULL), -- Otro reto complejo
(9, NULL), -- Un reto complejo más
(10, NULL); -- Otro ejemplo

INSERT INTO Subtarea (descripcion, estado, fecha_creacion, fecha_finalizada, reto_complejo_id) VALUES
('Leer el primer libro', 'PENDIENTE', '2024-01-01', NULL, 6),
('Leer el segundo libro', 'PENDIENTE', '2024-01-01', NULL, 6),
('Preparar un maratón', 'PENDIENTE', '2024-01-15', NULL, 7),
('Completar la primera carrera', 'PENDIENTE', '2024-01-10', NULL, 8),
('Visitar una ciudad nueva', 'PENDIENTE', '2024-02-01', NULL, 9);

INSERT INTO ProgresoReto (progreso_actual, fecha_actualizacion, usuario_id, reto_id) VALUES
(25.0, '2024-01-05', 1, 2), -- Juan tiene un progreso del 25% en su reto de correr 100 km
(50.0, '2024-01-07', 2, 1), -- Ana ha completado 2 de 5 libros en su reto
(0.0, '2024-01-05', 3, 5), -- Luis aún no ha empezado su reto de carreras
(10.0, '2024-01-04', 4, 3), -- María ha cocinado 1 receta de las 10
(0.0, '2024-01-03', 5, 4); -- Pedro aún no ha visitado ninguna ciudad

INSERT INTO Recompensa (descripcion, tipo, puntos, usuario_id, reto_id) VALUES
('Medalla de Bronce', 'Medalla', 10, 1, 2),
('Libro electrónico gratuito', 'Libro', 20, 2, 1),
('Descuento en tienda de deporte', 'Descuento', 30, 3, 5),
('Cupón de 20% en utensilios de cocina', 'Cupón', 15, 4, 3),
('Bono de viaje', 'Descuento', 50, 5, 4);

INSERT INTO Estadistica (total_retos, retos_completados, retos_fallidos, progreso_promedio, tiempo_promedio, usuario_id) VALUES
(5, 3, 1, 70.0, 15.0, 1),
(3, 2, 0, 80.0, 20.0, 2),
(2, 1, 0, 60.0, 18.0, 3),
(1, 0, 1, 10.0, 10.0, 4),
(4, 2, 1, 75.0, 25.0, 5);

INSERT INTO Notificacion (mensaje, leido, fecha_envio, usuario_id, reto_id) VALUES
('¡Has avanzado un 25% en tu reto de correr 100 km!', FALSE, '2024-01-05', 1, 2),
('¡Has leído 2 de 5 libros!', FALSE, '2024-01-07', 2, 1),
('¡Primera receta cocinada!', TRUE, '2024-01-04', 4, 3),
('No has iniciado tu reto de carreras', FALSE, '2024-01-05', 3, 5),
('No has visitado ninguna ciudad aún', FALSE, '2024-01-03', 5, 4);

INSERT INTO Comentario (texto, fecha, usuario_id, reto_id) VALUES
('Estoy disfrutando mucho este reto', '2024-01-05', 1, 2),
('Los libros seleccionados son increíbles', '2024-01-07', 2, 1),
('Espero poder cocinar todas las recetas a tiempo', '2024-01-04', 4, 3),
('Este reto es difícil, pero motivador', '2024-01-05', 3, 5),
('Aún no he comenzado, pero planeo hacerlo pronto', '2024-01-03', 5, 4);

INSERT INTO Amigos (usuario_id, amigo_id) VALUES
(1, 2), -- Juan es amigo de Ana
(2, 3), -- Ana es amiga de Luis
(3, 4), -- Luis es amigo de María
(4, 5), -- María es amiga de Pedro
(5, 1); -- Pedro es amigo de Juan

INSERT INTO ParticipantesReto (reto_id, usuario_id) VALUES
(1, 2), -- Ana está participando en el reto de lectura
(2, 1), -- Juan está participando en el reto de correr
(3, 4), -- María está participando en el reto de cocina
(4, 5), -- Pedro está participando en el reto de viajar
(5, 3); -- Luis está participando en el reto de carreras

INSERT INTO SubtareaParticipante (subtarea_id, usuario_id) VALUES
(1, 2), -- Ana está en la subtarea del primer libro
(2, 2), -- Ana está en la subtarea del segundo libro
(3, 1), -- Juan está en la subtarea de preparación de maratón
(4, 3), -- Luis está en la subtarea de completar la primera carrera
(5, 5); -- Pedro está en la subtarea de visitar una nueva ciudad
```

---

# Adicionales

## Herramientas de desarrollo

- **Java y JDK:** lenguaje de programación y kit de desarrollo
- **Maven:** herramienta de automatización de construcción utilizada principalmente en proyectos de desarrollo de software Java. Facilita la gestión de dependencias, la compilación del código y el empaquetado de aplicaciones.
- **Tomcat:** servidor web de código abierto y contenedor de servlets desarrollado por Apache Software Foundation, usado principalmente para ejecutar aplicaciones Java en entornos web, no es una base de datos como se indica erróneamente en el texto seleccionado
- **MYSQL:** base de datos relacional para las operaciones relacionales DDL, DML, DCL, DQL
- **JPA ORM Hibernate:**
- **Spring**
- **Spring Boot**
- **GIT:** Para llevar un control de versiones del proyecto.
- **XAMPP:** Para levantar el servidor de Apache para la administración de la base de datos.

---

## Acciones

- Retos diarios/mensuales
- Encontrar retos cerca de ti
- Retar a tus amigos
- Crear nuevo reto
    - Se asigna tiempo para completar
    - Nivel de dificultad
- Ranking de retos completados
- Unirse a retos

# Tareas asignadas

| Alfredo | Obiang |
| --- | --- |
| Modelo ER | Fichero SQL |
| Diagrama de clases | Inicio del código |
|  |  |