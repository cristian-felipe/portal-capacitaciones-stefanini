# Content Service - Portal de Capacitaciones Stefanini

## Descripción

Este servicio se encarga de sincronizar archivos de materiales con Amazon S3, basándose en la información almacenada en la tabla `capacitaciones.materiales`.

## Funcionalidad Principal

- **Sincronización automática**: Procesa archivos pendientes de migración a S3
- **Control de estados**: Estados detallados para el control de migración
- **Reintentos inteligentes**: Backoff exponencial con límite de reintentos
- **Validación de integridad**: Verificación de hash y tamaño de archivos
- **Configuración flexible**: Configuración parametrizable para diferentes entornos

## Configuración

### Variables de Entorno

```bash
# Base de datos
DB_USERNAME=admin-stefanini
DB_PASSWORD=stefanini123

# AWS S3
AWS_S3_BUCKET_NAME=portal-capacitaciones-bucket
AWS_S3_REGION=us-east-1
AWS_ACCESS_KEY=your-access-key
AWS_SECRET_KEY=your-secret-key

# Courses Service (ruta base para archivos)
COURSES_SERVICE_BASE_PATH=../../courses.service/courses.service

# Scheduler
SCHEDULER_ENABLED=true
SCHEDULER_CRON=0 */5 * * * ?  # Cada 5 minutos
SCHEDULER_BATCH_SIZE=10
```

## Proceso de Migración

1. **PENDING**: Material listo para migración
2. **IN_PROGRESS**: Migración en curso
3. **BACKUP_CREATED**: Backup del archivo creado
4. **VERIFIED**: Archivo verificado en S3
5. **LOCAL_DELETED**: Archivo local eliminado
6. **COMPLETED**: Migración completada exitosamente
7. **FAILED**: Migración fallida
8. **RETRY**: Programado para reintento

## API Endpoints

### Materiales

- `GET /api/materials/{id}/download` - **Descargar archivo directamente desde S3**

### Ejemplo de uso para el frontend:

```javascript
// Para descargar archivo
window.open('/api/materials/123/download', '_blank');

// O con fetch para más control
const response = await fetch('/api/materials/123/download');
const blob = await response.blob();
const url = window.URL.createObjectURL(blob);
const a = document.createElement('a');
a.href = url;
a.download = 'archivo.pdf';
a.click();
```

## Uso

### 1. Configurar variables de entorno

```bash
export AWS_S3_BUCKET_NAME=mi-bucket
export AWS_ACCESS_KEY=mi-access-key
export AWS_SECRET_KEY=mi-secret-key
export DB_USERNAME=admin-stefanini
export DB_PASSWORD=stefanini123
```

### 2. Ejecutar el servicio

```bash
mvn spring-boot:run
```

### 3. Verificar estado

```bash
curl http://localhost:8082/actuator/health
```

### 4. Probar endpoint

```bash
# Descargar archivo
curl -O http://localhost:8082/api/materials/1/download
```

## Estructura del Proyecto

```
src/main/java/com/stefanini/portal_capacitaciones/content/service/
├── config/          # Configuraciones
├── entity/          # Entidades JPA
├── repository/      # Repositorios
├── scheduler/       # Tareas programadas
└── service/         # Lógica de negocio
```

## Características Técnicas

- **Spring Boot 3.5.6**
- **PostgreSQL** para base de datos
- **AWS S3** para almacenamiento
- **Scheduler** para procesamiento automático
- **Logging** detallado para monitoreo