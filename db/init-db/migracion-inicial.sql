-- ======================================================
-- Script de inicialización de base de datos
-- Schemas: autenticacion, capacitaciones
-- Autor: Prueba técnica
-- ======================================================

-- Eliminar schemas si existen
DROP SCHEMA IF EXISTS autenticacion CASCADE;
DROP SCHEMA IF EXISTS capacitaciones CASCADE;

-- Crear schemas
CREATE SCHEMA autenticacion AUTHORIZATION "admin-stefanini";
CREATE SCHEMA capacitaciones AUTHORIZATION "admin-stefanini";

-- ============================
-- SCHEMA: AUTENTICACION
-- ============================

CREATE TABLE autenticacion.roles (
    id uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    nombre varchar(50) NOT NULL UNIQUE,
    descripcion text NULL,
    fecha_actualizacion timestamp(6) NULL,
    fecha_creacion timestamp(6) NULL
);

CREATE TABLE autenticacion.usuarios (
    id uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    correo_electronico varchar(255) NOT NULL UNIQUE,
    hash_contrasena varchar(255) NULL,
    nombre varchar(100) NOT NULL,
    apellido varchar(100) NOT NULL,
    rol varchar(20) DEFAULT 'usuario'::character varying NULL,
    proveedor_oauth varchar(50) NULL,
    id_oauth varchar(255) NULL,
    activo bool DEFAULT true NULL,
    fecha_creacion timestamp DEFAULT CURRENT_TIMESTAMP NULL,
    fecha_actualizacion timestamp DEFAULT CURRENT_TIMESTAMP NULL,
    CONSTRAINT usuarios_rol_check CHECK (
        rol::text = ANY (ARRAY['admin', 'instructor', 'usuario']::text[])
    )
);

CREATE TABLE autenticacion.usuarios_roles (
    usuario_id uuid NOT NULL,
    rol_id uuid NOT NULL,
    fecha_asignacion timestamp(6) NULL,
    PRIMARY KEY (usuario_id, rol_id),
    CONSTRAINT usuarios_roles_usuario_id_fkey FOREIGN KEY (usuario_id)
        REFERENCES autenticacion.usuarios(id) ON DELETE CASCADE,
    CONSTRAINT usuarios_roles_rol_id_fkey FOREIGN KEY (rol_id)
        REFERENCES autenticacion.roles(id) ON DELETE CASCADE
);

CREATE TABLE autenticacion.sesiones_usuarios (
    id uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    usuario_id uuid NULL,
    token varchar(512) NOT NULL,
    fecha_expiracion timestamp NOT NULL,
    fecha_creacion timestamp DEFAULT CURRENT_TIMESTAMP NULL,
    CONSTRAINT sesiones_usuarios_usuario_id_fkey FOREIGN KEY (usuario_id)
        REFERENCES autenticacion.usuarios(id)
);

CREATE INDEX idx_sesiones_token ON autenticacion.sesiones_usuarios(token);
CREATE INDEX idx_sesiones_usuario ON autenticacion.sesiones_usuarios(usuario_id);

-- ============================
-- SCHEMA: CAPACITACIONES
-- ============================

CREATE TABLE capacitaciones.programas (
    id serial PRIMARY KEY,
    titulo varchar(255) NOT NULL,
    descripcion text NULL,
    area_conocimiento varchar(100) NULL,
    fecha_creacion timestamp DEFAULT now() NULL
);

CREATE TABLE capacitaciones.unidades (
    id serial PRIMARY KEY,
    programa_id int4 NULL,
    titulo varchar(255) NOT NULL,
    orden int4 NULL,
    CONSTRAINT unidades_programa_id_fkey FOREIGN KEY (programa_id)
        REFERENCES capacitaciones.programas(id) ON DELETE CASCADE
);

CREATE TABLE capacitaciones.materiales (
    id serial PRIMARY KEY,
    activo bool NOT NULL,
    descripcion text NULL,
    extension varchar(10) NULL,
    fecha_subida timestamp(6) NOT NULL,
    nombre_archivo varchar(255) NOT NULL,
    nombre_original varchar(255) NOT NULL,
    ruta_archivo varchar(255) NOT NULL,
    tamaño_bytes bigint NULL,
    tipo_material varchar(50) NOT NULL,
    url_acceso varchar(255) NULL,
    migration_status varchar(20) NULL,
    migration_attempts int4 NULL,
    migration_last_attempt timestamp(6) NULL,
    migration_next_retry timestamp(6) NULL,
    file_hash varchar(255) NULL,
    file_size_verified bool NULL,
    local_file_deleted bool NULL,
    backup_created bool NULL,
    updated_at timestamp(6) NULL,
    s3_key varchar(255) NULL,
    s3_uploaded bool NULL,
    s3_upload_date timestamp(6) NULL,
    s3_error_message text NULL
);

CREATE INDEX idx_materiales_file_hash ON capacitaciones.materiales(file_hash);
CREATE INDEX idx_materiales_migration_attempts ON capacitaciones.materiales(migration_attempts);
CREATE INDEX idx_materiales_migration_next_retry ON capacitaciones.materiales(migration_next_retry);
CREATE INDEX idx_materiales_migration_status ON capacitaciones.materiales(migration_status);
CREATE INDEX idx_materiales_s3_key ON capacitaciones.materiales(s3_key);
CREATE INDEX idx_materiales_s3_uploaded ON capacitaciones.materiales(s3_uploaded);

CREATE TABLE capacitaciones.lecciones (
    id serial PRIMARY KEY,
    unidad_id int4 NULL,
    titulo varchar(255) NOT NULL,
    orden int4 NULL,
    tipo_material varchar(50) NULL,
    url_material text NULL,
    material_id int4 NULL UNIQUE,
    CONSTRAINT lecciones_unidad_id_fkey FOREIGN KEY (unidad_id)
        REFERENCES capacitaciones.unidades(id) ON DELETE CASCADE,
    CONSTRAINT lecciones_material_id_fkey FOREIGN KEY (material_id)
        REFERENCES capacitaciones.materiales(id)
);

CREATE TABLE capacitaciones.progreso (
    id bigserial PRIMARY KEY,
    usuario_id uuid NOT NULL,
    leccion_id int4 NULL,
    estado varchar(20) DEFAULT 'iniciado'::character varying NULL,
    porcentaje float8 DEFAULT 0 NULL,
    fecha_actualizacion timestamp DEFAULT now() NULL,
    CONSTRAINT progreso_usuario_id_leccion_id_key UNIQUE (usuario_id, leccion_id),
    CONSTRAINT progreso_leccion_id_fkey FOREIGN KEY (leccion_id)
        REFERENCES capacitaciones.lecciones(id) ON DELETE CASCADE
);

CREATE TABLE capacitaciones.insignias (
    id bigserial PRIMARY KEY,
    nombre varchar(255) NULL,
    descripcion text NULL,
    url_imagen text NULL
);

CREATE TABLE capacitaciones.insignias_otorgadas (
    id bigserial PRIMARY KEY,
    usuario_id uuid NOT NULL,
    insignia_id bigint NULL,
    fecha_otorgada timestamp DEFAULT now() NULL,
    CONSTRAINT insignias_otorgadas_insignia_id_fkey FOREIGN KEY (insignia_id)
        REFERENCES capacitaciones.insignias(id) ON DELETE CASCADE
);
