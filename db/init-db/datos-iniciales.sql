-- ======================================================
-- Datos de prueba iniciales
-- ======================================================

-- ============================
-- AUTENTICACION: Roles
-- ============================
INSERT INTO autenticacion.roles (id, nombre, descripcion, fecha_actualizacion, fecha_creacion)
VALUES
('5fb8a7dd-2c97-49c3-b1f9-73e47a347dc2'::uuid, 'admin', 'Acceso total', NULL, NULL),
('a4bff37d-42fc-49c2-885c-4e0bcc82e00c'::uuid, 'instructor', 'Puede crear y gestionar cursos', NULL, NULL),
('c8144930-18d4-4b90-86d8-c567c5a3bc63'::uuid, 'usuario', 'Acceso básico a cursos', NULL, NULL);

-- ============================
-- AUTENTICACION: Usuarios
-- ============================
INSERT INTO autenticacion.usuarios (id, correo_electronico, hash_contrasena, nombre, apellido, rol, activo, fecha_creacion, fecha_actualizacion)
VALUES
('b9646634-935f-4588-bc68-e89762df0d5d'::uuid, 'admin@portal.com', '$2a$10$69Y/j9l99rY.FEuakcQ11uIXX57clfDKfapvQbF2sy8fEtUmP7RgW', 'Administrador', 'Principal', 'admin', true, '2025-09-20 03:49:19.839', '2025-09-20 03:49:19.839'),
('937a5003-f8e6-411c-a6d7-7adefdc01629'::uuid, 'test@stefanini.com', '$2a$10$JBNmCCMp/fCM8BHeqM2eieUr6VpZfkH8k8cx59a1nqrrG0aZWOm1C', 'Usuario', 'Prueba', 'usuario', true, '2025-09-20 21:04:39.417', '2025-09-20 21:04:39.417'),
('d1234567-89ab-4cde-9012-3456789abcde'::uuid, 'usuario@portal.com', '$2a$10$69Y/j9l99rY.FEuakcQ11uIXX57clfDKfapvQbF2sy8fEtUmP7RgW', 'Usuario', 'Portal', 'usuario', true, '2025-09-21 02:29:35.646', '2025-09-21 02:29:35.646');

-- ============================
-- AUTENTICACION: Usuarios-Roles
-- ============================
INSERT INTO autenticacion.usuarios_roles (usuario_id, rol_id, fecha_asignacion)
VALUES
('b9646634-935f-4588-bc68-e89762df0d5d'::uuid, '5fb8a7dd-2c97-49c3-b1f9-73e47a347dc2'::uuid, now()),
('937a5003-f8e6-411c-a6d7-7adefdc01629'::uuid, 'c8144930-18d4-4b90-86d8-c567c5a3bc63'::uuid, now()),
('d1234567-89ab-4cde-9012-3456789abcde'::uuid, 'c8144930-18d4-4b90-86d8-c567c5a3bc63'::uuid, now());

-- ============================
-- CAPACITACIONES: Insignias
-- ============================
INSERT INTO capacitaciones.insignias (id, nombre, descripcion, url_imagen)
VALUES
(1, 'Badge Fullstack', 'Completó el curso Fullstack', 'https://example.com/badge_fullstack.png'),
(2, 'Badge Cloud', 'Completó el curso Cloud', 'https://example.com/badge_cloud.png');

-- ============================
-- CAPACITACIONES: Insignias Otorgadas
-- ============================
INSERT INTO capacitaciones.insignias_otorgadas (id, usuario_id, insignia_id, fecha_otorgada)
VALUES
(1, 'd1234567-89ab-4cde-9012-3456789abcde'::uuid, 1, '2025-09-21 04:07:22.423'),
(2, '550e8400-e29b-41d4-a716-446655440000'::uuid, 1, '2025-09-20 23:51:10.126'),
(3, '550e8400-e29b-41d4-a716-446655440000'::uuid, 2, '2025-09-20 23:51:23.433'),
(4, 'd1234567-89ab-4cde-9012-3456789abcde'::uuid, 2, '2025-09-21 05:13:53.257');

-- ============================
-- CAPACITACIONES: Materiales
-- ============================
INSERT INTO capacitaciones.materiales (id, activo, descripcion, extension, fecha_subida, nombre_archivo, nombre_original, ruta_archivo, tamaño_bytes, tipo_material, url_acceso, migration_status, migration_attempts, updated_at)
VALUES
(1, true, 'Documento de ejemplo para el curso', 'jpg', '2025-09-21 19:07:43.522', '1758499663498_330d782c.jpg', '7.jpg', 'uploads/1758499663498_330d782c.jpg', 1503203, 'imagen', '/courses-service/uploads/1758499663498_330d782c.jpg', 'FAILED', 0, '2025-09-21 21:05:00.653'),
(2, true, 'Documento de ejemplo para el curso', 'pdf', '2025-09-21 19:20:43.876', '1758500443836_99b85b78.pdf', 'pago-estudio-centrales-de-riesgo.pdf', 'uploads/1758500443836_99b85b78.pdf', 49815, 'pdf', '/courses-service/uploads/1758500443836_99b85b78.pdf', 'FAILED', 0, '2025-09-21 21:05:00.699'),
(3, true, 'Archivo para lección: PAGO CUOTA septiembre 2025 apto 1-2.pdf', 'pdf', '2025-09-21 19:30:35.104', '1758501035085_16bf0828.pdf', 'PAGO CUOTA septiembre 2025 apto 1-2.pdf', 'uploads/1758501035085_16bf0828.pdf', 43145, 'pdf', '/courses-service/uploads/1758501035085_16bf0828.pdf', 'FAILED', 0, '2025-09-21 21:05:00.704');

-- ============================
-- CAPACITACIONES: Programas
-- ============================
INSERT INTO capacitaciones.programas (id, titulo, descripcion, area_conocimiento, fecha_creacion)
VALUES
(1, 'Desarrollo Fullstack con Angular y Spring Boot', 'Curso completo de desarrollo fullstack utilizando Angular para el frontend y Spring Boot para el backend', 'Fullstack', '2025-09-20 17:57:17.644'),
(2, 'APIs e Integraciones - DataPower y IBM Bus', 'Curso especializado en integración de APIs utilizando DataPower y IBM Integration Bus', 'APIs e Integraciones', '2025-09-20 17:57:17.825'),
(3, 'Cloud Computing - AWS y Azure', 'Curso completo de computación en la nube con AWS y Microsoft Azure', 'Cloud', '2025-09-20 17:57:17.857'),
(4, 'Data Engineering con Python y Spark', 'Curso de ingeniería de datos utilizando Python, Apache Spark y herramientas de Big Data', 'Data Engineer', '2025-09-20 17:57:17.879');

-- ============================
-- CAPACITACIONES: Progreso
-- ============================
INSERT INTO capacitaciones.progreso (id, usuario_id, leccion_id, estado, porcentaje, fecha_actualizacion)
VALUES
(1, 'd1234567-89ab-4cde-9012-3456789abcde'::uuid, 1, 'completado', 21.3, '2025-09-21 04:06:28.101'),
(2, 'd1234567-89ab-4cde-9012-3456789abcde'::uuid, 2, 'en_progreso', 12.0, '2025-09-21 04:06:28.101'),
(3, 'd1234567-89ab-4cde-9012-3456789abcde'::uuid, 3, 'inscrito', 0.0, '2025-09-21 04:06:28.101');

-- ============================
-- CAPACITACIONES: Unidades 
-- ============================
INSERT INTO capacitaciones.unidades (id, programa_id, titulo, orden)
VALUES
(nextval('capacitaciones.unidades_id_seq'::regclass), 1, 'Unidad Frontend', 1),
(nextval('capacitaciones.unidades_id_seq'::regclass), 1, 'Unidad Backend', 2);

-- ======================================================
-- CAPACITACIONES: Lecciones
-- ======================================================

INSERT INTO capacitaciones.lecciones (id, unidad_id, titulo, orden, tipo_material, url_material, material_id)
VALUES
(1, 1, 'Introducción a Angular', 1, 'video', 'https://www.youtube.com/watch?v=example1', 1),
(2, 1, 'Componentes y Directivas', 2, 'pdf', 'https://example.com/componentes-angular.pdf', 2),
(3, 2, 'Configuración de Spring Boot', 1, 'video', 'https://www.youtube.com/watch?v=example2', 3),
(4, 2, 'REST APIs con Spring', 2, 'link', 'https://spring.io/guides/gs/rest-service/', 4),
(5, 3, 'Introducción a DataPower', 1, 'video', 'https://www.youtube.com/watch?v=datapower1', NULL),
(6, 3, 'Configuración de Políticas', 2, 'pdf', 'https://example.com/datapower-policies.pdf', NULL),
(7, 4, 'Arquitectura de IBM Bus', 1, 'video', 'https://www.youtube.com/watch?v=ibm-bus1', NULL),
(8, 5, 'Fundamentos de AWS', 1, 'video', 'https://www.youtube.com/watch?v=aws-fundamentals', NULL),
(9, 5, 'EC2 y S3', 2, 'link', 'https://aws.amazon.com/ec2/', NULL),
(43, 23, '¿Qué es Java?', 1, 'video', 'https://ejemplo.com/videos/java-intro.mp4', NULL),
(44, 23, 'Configuración del Entorno', 2, 'video', 'https://ejemplo.com/videos/java-setup.mp4', NULL),
(45, 24, 'Introducción a Spring Framework', 1, 'video', 'https://ejemplo.com/videos/spring-intro.mp4', NULL),
(46, 24, 'Creando tu Primera Aplicación', 2, 'pdf', 'https://ejemplo.com/pdf/spring-first-app.pdf', NULL),
(49, 26, 'Pandas y NumPy', 1, 'video', 'https://ejemplo.com/videos/java-intro.mp4', NULL),
(50, 26, 'Visualización con Matplotlib', 2, 'pdf', 'https://example.com/matplotlib-guide.pdf', NULL),
(51, 27, 'Introducción a Spark', 1, 'video', 'https://www.youtube.com/watch?v=spark-intro', NULL),
(52, 28, 'sdmas', 1, 'video', 'https://ejemplo.com/videos/java-intro.mp4', NULL);
