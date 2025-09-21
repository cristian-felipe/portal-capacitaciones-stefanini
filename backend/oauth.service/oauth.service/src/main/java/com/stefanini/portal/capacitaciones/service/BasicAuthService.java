package com.stefanini.portal.capacitaciones.service;

import com.stefanini.portal.capacitaciones.dto.LoginRequest;
import com.stefanini.portal.capacitaciones.dto.RegisterRequest;
import com.stefanini.portal.capacitaciones.entity.Usuario;
import com.stefanini.portal.capacitaciones.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class BasicAuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(BasicAuthService.class);
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordService passwordService;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private SesionService sesionService;
    
    /**
     * Registra un nuevo usuario con contraseña
     */
    public Map<String, Object> registerUser(RegisterRequest registerRequest) {
        try {
            logger.info("Intentando registrar usuario: {}", registerRequest.getEmail());
            
            // Verificar si el usuario ya existe
            if (usuarioRepository.existsByCorreoElectronico(registerRequest.getEmail())) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "El usuario ya existe con este email");
                return response;
            }
            
            // Validar contraseña
            if (!passwordService.isValidPassword(registerRequest.getPassword())) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "La contraseña debe tener al menos 6 caracteres, una letra y un número");
                return response;
            }
            
            // Crear nuevo usuario
            Usuario newUser = new Usuario();
            newUser.setCorreoElectronico(registerRequest.getEmail());
            newUser.setNombre(registerRequest.getNombre());
            newUser.setApellido(registerRequest.getApellido());
            newUser.setHashContrasena(passwordService.hashPassword(registerRequest.getPassword()));
            newUser.setRol(registerRequest.getRol());
            newUser.setActivo(true);
            
            // Guardar usuario
            newUser = usuarioRepository.save(newUser);
            
            // Generar token JWT
            String jwtToken = jwtService.generateToken(newUser);
            
            // Crear sesión
            sesionService.createSession(newUser, jwtToken);
            
            // Preparar respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("token", jwtToken);
            response.put("user", createUserResponse(newUser));
            response.put("message", "Usuario registrado exitosamente");
            
            logger.info("Usuario registrado exitosamente: {}", newUser.getCorreoElectronico());
            return response;
            
        } catch (Exception e) {
            logger.error("Error al registrar usuario: {}", e.getMessage(), e);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al registrar usuario: " + e.getMessage());
            return response;
        }
    }
    
    /**
     * Autentica un usuario con email y contraseña
     */
    public Map<String, Object> authenticateUser(LoginRequest loginRequest) {
        try {
            logger.info("Intentando autenticar usuario: {}", loginRequest.getEmail());
            
            // Buscar usuario por email
            Optional<Usuario> userOpt = usuarioRepository.findByCorreoElectronico(loginRequest.getEmail());
            
            if (userOpt.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Credenciales inválidas");
                return response;
            }
            
            Usuario user = userOpt.get();
            
            // Verificar si el usuario está activo
            if (!user.getActivo()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "El usuario está desactivado");
                return response;
            }
            
            // Verificar contraseña
            if (user.getHashContrasena() == null || 
                !passwordService.matches(loginRequest.getPassword(), user.getHashContrasena())) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Credenciales inválidas");
                return response;
            }
            
            // Generar token JWT
            String jwtToken = jwtService.generateToken(user);
            
            // Crear sesión
            sesionService.createSession(user, jwtToken);
            
            // Preparar respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("token", jwtToken);
            response.put("user", createUserResponse(user));
            response.put("message", "Autenticación exitosa");
            
            logger.info("Usuario autenticado exitosamente: {}", user.getCorreoElectronico());
            return response;
            
        } catch (Exception e) {
            logger.error("Error al autenticar usuario: {}", e.getMessage(), e);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error en la autenticación: " + e.getMessage());
            return response;
        }
    }
    
    /**
     * Cambia la contraseña de un usuario
     */
    public Map<String, Object> changePassword(String userEmail, String currentPassword, String newPassword) {
        try {
            logger.info("Intentando cambiar contraseña para usuario: {}", userEmail);
            
            // Buscar usuario
            Optional<Usuario> userOpt = usuarioRepository.findByCorreoElectronico(userEmail);
            
            if (userOpt.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Usuario no encontrado");
                return response;
            }
            
            Usuario user = userOpt.get();
            
            // Verificar contraseña actual
            if (user.getHashContrasena() == null || 
                !passwordService.matches(currentPassword, user.getHashContrasena())) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "La contraseña actual es incorrecta");
                return response;
            }
            
            // Validar nueva contraseña
            if (!passwordService.isValidPassword(newPassword)) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "La nueva contraseña debe tener al menos 6 caracteres, una letra y un número");
                return response;
            }
            
            // Actualizar contraseña
            user.setHashContrasena(passwordService.hashPassword(newPassword));
            usuarioRepository.save(user);
            
            // Invalidar todas las sesiones del usuario
            sesionService.deleteAllUserSessions(user);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Contraseña cambiada exitosamente. Por favor, inicia sesión nuevamente.");
            
            logger.info("Contraseña cambiada exitosamente para usuario: {}", userEmail);
            return response;
            
        } catch (Exception e) {
            logger.error("Error al cambiar contraseña: {}", e.getMessage(), e);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al cambiar contraseña: " + e.getMessage());
            return response;
        }
    }
    
    /**
     * Crea la respuesta del usuario sin información sensible
     */
    private Map<String, Object> createUserResponse(Usuario user) {
        Map<String, Object> userResponse = new HashMap<>();
        userResponse.put("id", user.getId());
        userResponse.put("email", user.getCorreoElectronico());
        userResponse.put("nombre", user.getNombre());
        userResponse.put("apellido", user.getApellido());
        userResponse.put("nombreCompleto", user.getNombreCompleto());
        userResponse.put("rol", user.getRol());
        userResponse.put("activo", user.getActivo());
        userResponse.put("fechaCreacion", user.getFechaCreacion());
        userResponse.put("proveedorOauth", user.getProveedorOauth());
        userResponse.put("permisos", getUserPermissions(user));
        return userResponse;
    }
    
    /**
     * Obtiene los permisos del usuario basado en su rol
     */
    private Map<String, Boolean> getUserPermissions(Usuario user) {
        Map<String, Boolean> permissions = new HashMap<>();
        
        permissions.put("canViewCourses", true);
        permissions.put("canEnrollInCourses", true);
        permissions.put("canViewProfile", true);
        
        if (user.isInstructor()) {
            permissions.put("canCreateCourses", true);
            permissions.put("canEditCourses", true);
            permissions.put("canViewStudents", true);
        }
        
        if (user.isAdmin()) {
            permissions.put("canManageUsers", true);
            permissions.put("canManageSystem", true);
            permissions.put("canViewReports", true);
            permissions.put("canCreateCourses", true);
            permissions.put("canEditCourses", true);
            permissions.put("canViewStudents", true);
        }
        
        return permissions;
    }
}

