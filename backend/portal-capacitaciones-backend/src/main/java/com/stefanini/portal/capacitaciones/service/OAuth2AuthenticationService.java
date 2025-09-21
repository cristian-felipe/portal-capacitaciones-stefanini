package com.stefanini.portal.capacitaciones.service;

import com.stefanini.portal.capacitaciones.entity.Rol;
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
public class OAuth2AuthenticationService {
    
    private static final Logger logger = LoggerFactory.getLogger(OAuth2AuthenticationService.class);
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private SesionService sesionService;
    
    /**
     * Procesa la autenticación OAuth2 completa
     */
    public Map<String, Object> authenticateOAuth2User(OAuth2UserInfo userInfo) {
        try {
            logger.info("Iniciando autenticación OAuth2 para usuario: {}", userInfo.getEmail());
            
            // Buscar usuario existente
            Optional<Usuario> existingUser = findUserByOAuthInfo(userInfo);
            
            Usuario user;
            boolean isNewUser = false;
            
            if (existingUser.isPresent()) {
                user = existingUser.get();
                logger.info("Usuario existente encontrado: {}", user.getCorreoElectronico());
                
                // Actualizar información si es necesario
                user = updateUserOAuthInfo(user, userInfo);
                
            } else {
                // Crear nuevo usuario
                user = createNewOAuth2User(userInfo);
                isNewUser = true;
                logger.info("Nuevo usuario creado: {}", user.getCorreoElectronico());
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
            response.put("isNewUser", isNewUser);
            response.put("message", isNewUser ? "Usuario creado y autenticado exitosamente" : "Autenticación exitosa");
            
            logger.info("Autenticación OAuth2 completada para usuario: {}", user.getCorreoElectronico());
            return response;
            
        } catch (Exception e) {
            logger.error("Error en autenticación OAuth2: {}", e.getMessage(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error en la autenticación: " + e.getMessage());
            return errorResponse;
        }
    }
    
    /**
     * Busca un usuario por información OAuth2
     */
    private Optional<Usuario> findUserByOAuthInfo(OAuth2UserInfo userInfo) {
        // Buscar por ID de OAuth primero
        Optional<Usuario> userByOAuth = usuarioRepository.findByProveedorOauthAndIdOauth(
            userInfo.getProvider(), 
            userInfo.getOauthId()
        );
        
        if (userByOAuth.isPresent()) {
            return userByOAuth;
        }
        
        // Si no se encuentra por OAuth ID, buscar por email
        return usuarioRepository.findByCorreoElectronico(userInfo.getEmail());
    }
    
    /**
     * Crea un nuevo usuario desde información OAuth2
     */
    private Usuario createNewOAuth2User(OAuth2UserInfo userInfo) {
        Usuario newUser = new Usuario();
        newUser.setCorreoElectronico(userInfo.getEmail());
        newUser.setNombre(userInfo.getFirstName());
        newUser.setApellido(userInfo.getLastName());
        newUser.setRol(determineUserRole(userInfo));
        newUser.setProveedorOauth(userInfo.getProvider());
        newUser.setIdOauth(userInfo.getOauthId());
        newUser.setActivo(true);
        
        return usuarioRepository.save(newUser);
    }
    
    /**
     * Actualiza la información OAuth2 de un usuario existente
     */
    private Usuario updateUserOAuthInfo(Usuario user, OAuth2UserInfo userInfo) {
        boolean needsUpdate = false;
        
        // Actualizar información OAuth si no existe
        if (user.getProveedorOauth() == null || user.getIdOauth() == null) {
            user.setProveedorOauth(userInfo.getProvider());
            user.setIdOauth(userInfo.getOauthId());
            needsUpdate = true;
        }
        
        // Actualizar nombre si está vacío o es diferente
        if (user.getNombre() == null || user.getNombre().isEmpty()) {
            user.setNombre(userInfo.getFirstName());
            needsUpdate = true;
        }
        
        if (user.getApellido() == null || user.getApellido().isEmpty()) {
            user.setApellido(userInfo.getLastName());
            needsUpdate = true;
        }
        
        if (needsUpdate) {
            user = usuarioRepository.save(user);
        }
        
        return user;
    }
    
    /**
     * Determina el rol del usuario basado en información OAuth2
     */
    private Rol determineUserRole(OAuth2UserInfo userInfo) {
        // Lógica para determinar rol basado en email o dominio
        String email = userInfo.getEmail().toLowerCase();
        
        if (email.contains("@stefanini.com")) {
            // Usuarios de Stefanini pueden ser admin o instructor
            if (email.contains("admin") || email.contains("administrator")) {
                return Rol.ADMIN;
            } else if (email.contains("instructor") || email.contains("trainer")) {
                return Rol.INSTRUCTOR;
            }
        }
        
        // Rol por defecto
        return Rol.USUARIO;
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
        userResponse.put("rol", user.getRol().getValor());
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
    
    /**
     * Valida si un usuario puede autenticarse
     */
    public boolean canUserAuthenticate(String email) {
        Optional<Usuario> user = usuarioRepository.findByCorreoElectronico(email);
        return user.isPresent() && user.get().getActivo();
    }
    
    /**
     * Obtiene información del usuario por email
     */
    public Optional<Usuario> getUserByEmail(String email) {
        return usuarioRepository.findByCorreoElectronico(email);
    }
}

