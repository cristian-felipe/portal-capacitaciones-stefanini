# Portal de Capacitaciones - Servicio OAuth

## Descripción

Este servicio proporciona autenticación y autorización para el Portal de Capacitaciones de Stefanini. Implementa autenticación básica con email/contraseña y soporte para OAuth2.

## Arquitectura

### Principios de Clean Code Aplicados

1. **Constantes Centralizadas**: Todos los strings mágicos y mensajes están centralizados en `AuthConstants`
2. **DTOs Consistentes**: Uso de DTOs específicos para requests y responses
3. **Manejo de Excepciones**: Excepciones personalizadas con manejo global
4. **Validación Separada**: Lógica de validación extraída a clases especializadas
5. **Principios SOLID**: Servicios con responsabilidades únicas y bien definidas

### Estructura del Proyecto

```
src/main/java/com/stefanini/portal/capacitaciones/
├── constants/
│   └── AuthConstants.java          # Constantes centralizadas
├── config/
│   └── SecurityConfig.java         # Configuración de seguridad
├── controller/
│   ├── BasicAuthController.java    # Controlador de autenticación básica
│   └── OAuth2Controller.java       # Controlador de OAuth2
├── dto/
│   ├── AuthResponse.java           # DTO de respuesta de autenticación
│   ├── UserResponse.java           # DTO de respuesta de usuario
│   ├── LoginRequest.java           # DTO de solicitud de login
│   ├── RegisterRequest.java        # DTO de solicitud de registro
│   └── ChangePasswordRequest.java  # DTO de solicitud de cambio de contraseña
├── entity/
│   ├── Usuario.java                # Entidad de usuario
│   ├── RolEntity.java              # Entidad de rol
│   ├── UsuarioRol.java             # Entidad de relación usuario-rol
│   ├── UsuarioRolId.java           # ID compuesto para usuario-rol
│   └── SesionUsuario.java          # Entidad de sesión de usuario
├── exception/
│   ├── AuthException.java          # Excepción base de autenticación
│   ├── UserNotFoundException.java  # Usuario no encontrado
│   ├── InvalidCredentialsException.java # Credenciales inválidas
│   ├── UserAlreadyExistsException.java  # Usuario ya existe
│   ├── PasswordValidationException.java # Error de validación de contraseña
│   └── GlobalExceptionHandler.java # Manejador global de excepciones
├── repository/
│   ├── UsuarioRepository.java      # Repositorio de usuarios
│   ├── RolRepository.java          # Repositorio de roles
│   ├── UsuarioRolRepository.java   # Repositorio de usuario-rol
│   └── SesionUsuarioRepository.java # Repositorio de sesiones
├── service/
│   ├── BasicAuthService.java       # Servicio de autenticación básica
│   ├── JwtService.java             # Servicio de JWT
│   ├── PasswordService.java        # Servicio de contraseñas
│   ├── SesionService.java          # Servicio de sesiones
│   ├── UsuarioService.java         # Servicio de usuarios
│   └── RolService.java             # Servicio de roles
└── validation/
    └── PasswordValidator.java      # Validador de contraseñas
```

## Características Principales

### Autenticación Básica
- Registro de usuarios con validación de contraseñas
- Login con email y contraseña
- Cambio de contraseñas
- Generación de tokens JWT

### Seguridad
- Encriptación de contraseñas con BCrypt
- Tokens JWT con expiración configurable
- Validación de contraseñas robusta
- Manejo seguro de sesiones

### Validaciones
- Validación de formato de email
- Validación de fortaleza de contraseñas
- Validación de datos de entrada con Bean Validation

### Manejo de Errores
- Excepciones personalizadas para diferentes tipos de errores
- Manejador global de excepciones
- Respuestas consistentes de error
- Logging detallado de errores

## Endpoints Disponibles

### Autenticación Básica (`/auth/basic`)

- `POST /register` - Registrar nuevo usuario
- `POST /login` - Autenticar usuario
- `POST /change-password` - Cambiar contraseña
- `POST /test-register` - Registrar usuario de prueba
- `POST /test-login` - Login de usuario de prueba

### OAuth2 (`/auth/oauth2`)

- `GET /login` - Endpoint de login OAuth2
- `GET /success` - Página de éxito OAuth2
- `GET /failure` - Página de fallo OAuth2

## Configuración

### Variables de Entorno

```properties
# JWT Configuration
jwt.secret=tu-clave-secreta-muy-segura
jwt.expiration=86400000

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/portal_capacitaciones
spring.datasource.username=usuario
spring.datasource.password=contraseña
```

### Dependencias Principales

- Spring Boot 3.5.6
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT (jjwt)
- Lombok
- SpringDoc OpenAPI

## Mejoras de Clean Code Implementadas

### 1. Constantes Centralizadas
- Eliminación de strings mágicos
- Mensajes de error consistentes
- Configuración centralizada de endpoints

### 2. DTOs Específicos
- `AuthResponse` para respuestas de autenticación
- `UserResponse` para datos de usuario sin información sensible
- `ChangePasswordRequest` para cambio de contraseñas

### 3. Excepciones Personalizadas
- Jerarquía de excepciones específicas
- Códigos de error consistentes
- Manejo global centralizado

### 4. Validación Separada
- `PasswordValidator` para validación de contraseñas
- Lógica de validación reutilizable
- Mensajes de error específicos

### 5. Principios SOLID
- **Single Responsibility**: Cada clase tiene una responsabilidad única
- **Open/Closed**: Extensible sin modificar código existente
- **Liskov Substitution**: Excepciones intercambiables
- **Interface Segregation**: Interfaces específicas
- **Dependency Inversion**: Inyección de dependencias

## Testing

### Endpoints de Prueba
- `/auth/basic/test-register` - Registra usuario de prueba
- `/auth/basic/test-login` - Login de usuario de prueba

### Usuario de Prueba
- Email: `test@stefanini.com`
- Contraseña: `test123`

## Documentación API

La documentación de la API está disponible en:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Logging

El servicio implementa logging detallado para:
- Operaciones de autenticación
- Errores de validación
- Cambios de contraseña
- Creación de sesiones

## Seguridad

### Medidas Implementadas
- Encriptación de contraseñas con BCrypt
- Tokens JWT con firma HMAC
- Validación de entrada con Bean Validation
- CORS configurado apropiadamente
- CSRF deshabilitado para APIs REST

### Recomendaciones
- Usar HTTPS en producción
- Rotar claves JWT regularmente
- Implementar rate limiting
- Monitorear intentos de login fallidos
