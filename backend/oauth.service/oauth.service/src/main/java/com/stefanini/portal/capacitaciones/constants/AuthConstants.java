package com.stefanini.portal.capacitaciones.constants;

public final class AuthConstants {
    
    // Roles
    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_INSTRUCTOR = "instructor";
    public static final String ROLE_USER = "usuario";
    
    // Mensajes de respuesta
    public static final String SUCCESS = "success";
    public static final String MESSAGE = "message";
    public static final String TOKEN = "token";
    public static final String USER = "user";
    
    // Mensajes de éxito
    public static final String USER_REGISTERED_SUCCESSFULLY = "Usuario registrado exitosamente";
    public static final String USER_AUTHENTICATED_SUCCESSFULLY = "Autenticación exitosa";
    public static final String PASSWORD_CHANGED_SUCCESSFULLY = "Contraseña cambiada exitosamente. Por favor, inicia sesión nuevamente.";
    public static final String OAUTH2_LOGIN_SUCCESSFUL = "OAuth2 login successful";
    public static final String OAUTH2_LOGIN_ENDPOINT = "OAuth2 login endpoint";
    
    // Mensajes de error
    public static final String USER_ALREADY_EXISTS = "El usuario ya existe con este email";
    public static final String INVALID_CREDENTIALS = "Credenciales inválidas";
    public static final String USER_DISABLED = "El usuario está desactivado";
    public static final String USER_NOT_FOUND = "Usuario no encontrado";
    public static final String INVALID_CURRENT_PASSWORD = "La contraseña actual es incorrecta";
    public static final String OAUTH2_LOGIN_FAILED = "OAuth2 login failed";
    
    // Mensajes de validación
    public static final String PASSWORD_VALIDATION_ERROR = "La contraseña debe tener al menos 6 caracteres, una letra y un número";
    public static final String NEW_PASSWORD_VALIDATION_ERROR = "La nueva contraseña debe tener al menos 6 caracteres, una letra y un número";
    public static final String MISSING_PARAMETERS = "Faltan parámetros: email, currentPassword, newPassword";
    
    // Mensajes de error genéricos
    public static final String REGISTRATION_ERROR = "Error al registrar usuario: ";
    public static final String AUTHENTICATION_ERROR = "Error en la autenticación: ";
    public static final String PASSWORD_CHANGE_ERROR = "Error al cambiar contraseña: ";
    
    // Claims JWT
    public static final String CLAIM_USER_ID = "userId";
    public static final String CLAIM_EMAIL = "email";
    public static final String CLAIM_ROLE = "role";
    public static final String CLAIM_NOMBRE = "nombre";
    public static final String CLAIM_APELLIDO = "apellido";
    
    // Permisos
    public static final String PERMISSION_VIEW_COURSES = "canViewCourses";
    public static final String PERMISSION_ENROLL_IN_COURSES = "canEnrollInCourses";
    public static final String PERMISSION_VIEW_PROFILE = "canViewProfile";
    public static final String PERMISSION_CREATE_COURSES = "canCreateCourses";
    public static final String PERMISSION_EDIT_COURSES = "canEditCourses";
    public static final String PERMISSION_VIEW_STUDENTS = "canViewStudents";
    public static final String PERMISSION_MANAGE_USERS = "canManageUsers";
    public static final String PERMISSION_MANAGE_SYSTEM = "canManageSystem";
    public static final String PERMISSION_VIEW_REPORTS = "canViewReports";
    
    // Endpoints
    public static final String AUTH_BASIC_PATH = "/auth/basic";
    public static final String AUTH_OAUTH2_PATH = "/auth/oauth2";
    public static final String OAUTH2_PATH = "/oauth2";
    public static final String LOGIN_OAUTH2_PATH = "/login/oauth2";
    public static final String TEST_PATH = "/test";
    public static final String SWAGGER_UI_PATH = "/swagger-ui/**";
    public static final String API_DOCS_PATH = "/api-docs/**";
    public static final String V3_API_DOCS_PATH = "/v3/api-docs/**";
    public static final String ACTUATOR_HEALTH_PATH = "/actuator/health";
    
    // Headers
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    
    // Test data
    public static final String TEST_EMAIL = "test@stefanini.com";
    public static final String TEST_PASSWORD = "test123";
    public static final String TEST_NOMBRE = "Usuario";
    public static final String TEST_APELLIDO = "Prueba";
    
    // Status
    public static final String STATUS_AVAILABLE = "available";
    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_FAILURE = "failure";
    public static final String TEST_MODE = "testMode";
    
    private AuthConstants() {
        throw new UnsupportedOperationException("Esta clase no debe ser instanciada");
    }
}
