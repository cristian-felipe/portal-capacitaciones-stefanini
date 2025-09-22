package com.stefanini.portal.capacitaciones.service;

import com.stefanini.portal.capacitaciones.constants.AuthConstants;
import com.stefanini.portal.capacitaciones.dto.AuthResponse;
import com.stefanini.portal.capacitaciones.dto.LoginRequest;
import com.stefanini.portal.capacitaciones.dto.RegisterRequest;
import com.stefanini.portal.capacitaciones.dto.UserResponse;
import com.stefanini.portal.capacitaciones.entity.Usuario;
import com.stefanini.portal.capacitaciones.exception.InvalidCredentialsException;
import com.stefanini.portal.capacitaciones.exception.UserAlreadyExistsException;
import com.stefanini.portal.capacitaciones.exception.UserNotFoundException;
import com.stefanini.portal.capacitaciones.repository.UsuarioRepository;
import com.stefanini.portal.capacitaciones.validation.PasswordValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    
    @Autowired
    private PasswordValidator passwordValidator;
    
    public AuthResponse registerUser(RegisterRequest registerRequest) {
        logger.info("Intentando registrar usuario: {}", registerRequest.getEmail());
        
        if (usuarioRepository.existsByCorreoElectronico(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException(AuthConstants.USER_ALREADY_EXISTS);
        }
        
        passwordValidator.isValidPassword(registerRequest.getPassword());
        
        Usuario newUser = createNewUser(registerRequest);
        newUser = usuarioRepository.save(newUser);
        
        String jwtToken = jwtService.generateToken(newUser);
        sesionService.createSession(newUser, jwtToken);
        
        logger.info("Usuario registrado exitosamente: {}", newUser.getCorreoElectronico());
        return AuthResponse.userRegistered(jwtToken, UserResponse.from(newUser));
    }
    
    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        logger.info("Intentando autenticar usuario: {}", loginRequest.getEmail());
        
        Usuario user = findUserByEmail(loginRequest.getEmail());
        validateUserIsActive(user);
        validatePassword(loginRequest.getPassword(), user.getHashContrasena());
        
        String jwtToken = jwtService.generateToken(user);
        sesionService.createSession(user, jwtToken);
        
        logger.info("Usuario autenticado exitosamente: {}", user.getCorreoElectronico());
        return AuthResponse.userAuthenticated(jwtToken, UserResponse.from(user));
    }
    
    public AuthResponse changePassword(String userEmail, String currentPassword, String newPassword) {
        logger.info("Intentando cambiar contraseña para usuario: {}", userEmail);
        
        Usuario user = findUserByEmail(userEmail);
        validatePassword(currentPassword, user.getHashContrasena());
        passwordValidator.validatePasswordChange(currentPassword, newPassword);
        
        user.setHashContrasena(passwordService.hashPassword(newPassword));
        usuarioRepository.save(user);
        sesionService.deleteAllUserSessions(user);
        
        logger.info("Contraseña cambiada exitosamente para usuario: {}", userEmail);
        return AuthResponse.passwordChanged();
    }
    
    private Usuario createNewUser(RegisterRequest registerRequest) {
        Usuario newUser = new Usuario();
        newUser.setCorreoElectronico(registerRequest.getEmail());
        newUser.setNombre(registerRequest.getNombre());
        newUser.setApellido(registerRequest.getApellido());
        newUser.setHashContrasena(passwordService.hashPassword(registerRequest.getPassword()));
        newUser.setRol(registerRequest.getRol() != null ? registerRequest.getRol() : AuthConstants.ROLE_USER);
        newUser.setActivo(true);
        return newUser;
    }
    
    private Usuario findUserByEmail(String email) {
        return usuarioRepository.findByCorreoElectronico(email)
                .orElseThrow(() -> new UserNotFoundException(AuthConstants.USER_NOT_FOUND));
    }
    
    private void validateUserIsActive(Usuario user) {
        if (!user.getActivo()) {
            throw new InvalidCredentialsException(AuthConstants.USER_DISABLED);
        }
    }
    
    private void validatePassword(String password, String hashedPassword) {
        if (hashedPassword == null || !passwordService.matches(password, hashedPassword)) {
            throw new InvalidCredentialsException(AuthConstants.INVALID_CREDENTIALS);
        }
    }
}

