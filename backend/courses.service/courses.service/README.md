# 🎓 Portal de Capacitaciones - Courses Service

## 📋 Descripción

Servicio backend para el Portal de Capacitaciones de Stefanini. Este microservicio maneja la gestión completa de cursos, incluyendo programas, unidades, lecciones y materiales de aprendizaje.

## 🏗️ Arquitectura

### **Tecnologías Utilizadas:**
- **Java 17**
- **Spring Boot 3.5.6**
- **Spring Data JPA**
- **PostgreSQL**
- **Maven**
- **Swagger/OpenAPI**

### **Estructura del Proyecto:**
```
src/main/java/com/stefanini/portal/capacitaciones/
├── controller/          # Controladores REST
├── service/            # Lógica de negocio
├── repository/         # Acceso a datos
├── entity/            # Entidades JPA
├── dto/               # Objetos de transferencia
└── config/            # Configuraciones
```

## 🚀 Funcionalidades

### **Gestión de Cursos:**
- ✅ Crear programas completos con unidades y lecciones
- ✅ Gestión de lecciones con materiales
- ✅ Subida y gestión de archivos (PDF, video, imágenes)
- ✅ Sistema de insignias y gamificación
- ✅ Seguimiento de progreso de usuarios

### **Endpoints Principales:**

#### **Programas Completos:**
```bash
POST   /api/programas-completos          # Crear programa completo
GET    /api/programas-completos          # Listar todos los programas
GET    /api/programas-completos/{id}     # Obtener programa por ID
PUT    /api/programas-completos/{id}     # Actualizar programa
DELETE /api/programas-completos/{id}     # Eliminar programa
```

#### **Lecciones:**
```bash
GET    /api/lecciones/unidad/{id}/completo    # Lecciones con materiales
GET    /api/lecciones/{id}/con-material       # Lección con material completo
POST   /api/lecciones                        # Crear lección
PUT    /api/lecciones/{id}                   # Actualizar lección
```

#### **Gestión de Archivos:**
```bash
POST   /api/archivos/subir                  # Subir archivo
GET    /api/archivos                        # Listar archivos
DELETE /api/archivos/{id}                   # Eliminar archivo
GET    /api/archivos/estadisticas          # Estadísticas de almacenamiento
```

#### **Insignias:**
```bash
GET    /api/insignias                       # Listar insignias
POST   /api/insignias                       # Crear insignia
```

#### **Progreso:**
```bash
GET    /api/progreso/usuario/{userId}       # Progreso del usuario
POST   /api/progreso                        # Registrar progreso
```

## 🗄️ Base de Datos

### **Esquema Principal:**
- **`programas`** - Programas de capacitación
- **`unidades`** - Unidades dentro de programas
- **`lecciones`** - Lecciones dentro de unidades
- **`materiales`** - Archivos subidos
- **`insignias`** - Sistema de gamificación
- **`progreso`** - Seguimiento de usuarios

### **Relaciones:**
```
Programa (1) → (N) Unidades
Unidad (1) → (N) Lecciones
Leccion (1) → (1) Material (opcional)
```

## ⚙️ Configuración

### **Variables de Entorno:**
```properties
# Base de datos
spring.datasource.url=jdbc:postgresql://localhost:5432/capacitaciones
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password

# Servidor
server.port=8081
server.servlet.context-path=/courses-service

# Archivos
app.file.upload-dir=uploads/
app.file.max-size=10MB
```

### **Tipos de Archivo Soportados:**
- **PDF**: Documentos y presentaciones
- **Video**: MP4, AVI, MOV
- **Imagen**: JPG, PNG, GIF
- **Otros**: DOC, DOCX, XLS, XLSX

## 🚀 Instalación y Ejecución

### **Prerrequisitos:**
- Java 17+
- Maven 3.6+
- PostgreSQL 12+

### **Pasos:**
1. **Clonar el repositorio**
2. **Configurar la base de datos**
3. **Ejecutar el proyecto:**
```bash
mvn spring-boot:run
```

4. **Acceder a la documentación:**
```
http://localhost:8081/courses-service/swagger-ui.html
```

## 📊 Características Técnicas

### **Performance:**
- ✅ Consultas optimizadas con JPA
- ✅ Carga lazy de relaciones
- ✅ Paginación automática
- ✅ Caché de consultas frecuentes

### **Seguridad:**
- ✅ Validación de entrada
- ✅ Manejo de excepciones
- ✅ CORS configurado
- ✅ Validación de tipos de archivo

### **Escalabilidad:**
- ✅ Arquitectura de microservicios
- ✅ Separación de responsabilidades
- ✅ Preparado para S3 (futuro)
- ✅ Sistema de migración de archivos

## 🔧 Desarrollo

### **Estructura de Commits:**
```
feat: nueva funcionalidad
fix: corrección de bug
docs: documentación
style: formato de código
refactor: refactorización
test: pruebas
```

### **Testing:**
```bash
mvn test                    # Ejecutar pruebas unitarias
mvn integration-test        # Pruebas de integración
```

## 📈 Roadmap

### **Versión Actual (1.0.0):**
- ✅ CRUD completo de cursos
- ✅ Gestión de archivos local
- ✅ Sistema de insignias
- ✅ Seguimiento de progreso

### **Próximas Versiones:**
- 🔄 Migración a AWS S3
- 🔄 Notificaciones push
- 🔄 Análisis de aprendizaje
- 🔄 Integración con LMS

## 👥 Contribución

1. Fork el proyecto
2. Crear rama para feature (`git checkout -b feature/AmazingFeature`)
3. Commit cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir Pull Request

## 📄 Licencia

Este proyecto es propiedad de Stefanini Group.

## 📞 Contacto

- **Equipo de Desarrollo**: Portal Capacitaciones
- **Email**: portal.capacitaciones@stefanini.com
- **Documentación**: [Wiki Interno]

---

**Portal de Capacitaciones Stefanini** - Transformando el aprendizaje corporativo 🚀
