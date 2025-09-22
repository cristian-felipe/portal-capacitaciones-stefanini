package com.stefanini.portal.capacitaciones.controller;

import com.stefanini.portal.capacitaciones.constants.AuthConstants;
import com.stefanini.portal.capacitaciones.dto.AuthResponse;
import com.stefanini.portal.capacitaciones.dto.ChangePasswordRequest;
import com.stefanini.portal.capacitaciones.dto.LoginRequest;
import com.stefanini.portal.capacitaciones.dto.RegisterRequest;
import com.stefanini.portal.capacitaciones.service.BasicAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AuthConstants.AUTH_BASIC_PATH)
@Tag(name = "Autenticación Básica", description = "APIs para autenticación con email y contraseña")
@CrossOrigin(origins = "*")
public class BasicAuthController {
    
    private final BasicAuthService basicAuthService;
    
    @Autowired
    public BasicAuthController(BasicAuthService basicAuthService) {
        this.basicAuthService = basicAuthService;
    }
    
    @PostMapping("/register")
    @Operation(summary = "Registrar usuario", description = "Registra un nuevo usuario con email y contraseña")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        AuthResponse response = basicAuthService.registerUser(registerRequest);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    @Operation(summary = "Login con email y contraseña", description = "Autentica un usuario con email y contraseña")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse response = basicAuthService.authenticateUser(loginRequest);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/change-password")
    @Operation(summary = "Cambiar contraseña", description = "Cambia la contraseña del usuario autenticado")
    public ResponseEntity<AuthResponse> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        AuthResponse response = basicAuthService.changePassword(
            changePasswordRequest.getEmail(),
            changePasswordRequest.getCurrentPassword(),
            changePasswordRequest.getNewPassword()
        );
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/test-register")
    @Operation(summary = "Test Register", description = "Registra un usuario de prueba para testing")
    public ResponseEntity<AuthResponse> testRegister() {
        RegisterRequest testUser = createTestUser();
        AuthResponse response = basicAuthService.registerUser(testUser);
        return ResponseEntity.ok(response.withAdditionalData(AuthConstants.TEST_MODE, true));
    }
    
    @PostMapping("/test-login")
    @Operation(summary = "Test Login", description = "Login de prueba con usuario test@stefanini.com")
    public ResponseEntity<AuthResponse> testLogin() {
        LoginRequest testLogin = createTestLoginRequest();
        AuthResponse response = basicAuthService.authenticateUser(testLogin);
        return ResponseEntity.ok(response.withAdditionalData(AuthConstants.TEST_MODE, true));
    }
    
    private RegisterRequest createTestUser() {
        RegisterRequest testUser = new RegisterRequest();
        testUser.setNombre(AuthConstants.TEST_NOMBRE);
        testUser.setApellido(AuthConstants.TEST_APELLIDO);
        testUser.setEmail(AuthConstants.TEST_EMAIL);
        testUser.setPassword(AuthConstants.TEST_PASSWORD);
        return testUser;
    }
    
    private LoginRequest createTestLoginRequest() {
        LoginRequest testLogin = new LoginRequest();
        testLogin.setEmail(AuthConstants.TEST_EMAIL);
        testLogin.setPassword(AuthConstants.TEST_PASSWORD);
        return testLogin;
    }
}



