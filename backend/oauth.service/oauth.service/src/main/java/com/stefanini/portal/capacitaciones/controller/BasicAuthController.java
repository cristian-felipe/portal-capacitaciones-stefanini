package com.stefanini.portal.capacitaciones.controller;

import com.stefanini.portal.capacitaciones.dto.LoginRequest;
import com.stefanini.portal.capacitaciones.dto.RegisterRequest;
import com.stefanini.portal.capacitaciones.service.BasicAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth/basic")
@Tag(name = "Autenticación Básica", description = "APIs para autenticación con email y contraseña")
@CrossOrigin(origins = "*")
public class BasicAuthController {
    
    @Autowired
    private BasicAuthService basicAuthService;
    
    @PostMapping("/register")
    @Operation(summary = "Registrar usuario", description = "Registra un nuevo usuario con email y contraseña")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        Map<String, Object> result = basicAuthService.registerUser(registerRequest);
        
        if ((Boolean) result.get("success")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
    
    @PostMapping("/login")
    @Operation(summary = "Login con email y contraseña", description = "Autentica un usuario con email y contraseña")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        Map<String, Object> result = basicAuthService.authenticateUser(loginRequest);
        
        if ((Boolean) result.get("success")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
    
    @PostMapping("/change-password")
    @Operation(summary = "Cambiar contraseña", description = "Cambia la contraseña del usuario autenticado")
    public ResponseEntity<?> changePassword(
            @RequestHeader("Authorization") String authorization,
            @RequestBody Map<String, String> passwordRequest) {
        
        try {
            String token = authorization.replace("Bearer ", "");
            
            // Extraer email del token (necesitarías implementar esto en JwtService)
            String userEmail = passwordRequest.get("email");
            String currentPassword = passwordRequest.get("currentPassword");
            String newPassword = passwordRequest.get("newPassword");
            
            if (userEmail == null || currentPassword == null || newPassword == null) {
                Map<String, Object> response = Map.of(
                    "success", false,
                    "message", "Faltan parámetros: email, currentPassword, newPassword"
                );
                return ResponseEntity.badRequest().body(response);
            }
            
            Map<String, Object> result = basicAuthService.changePassword(userEmail, currentPassword, newPassword);
            
            if ((Boolean) result.get("success")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
            
        } catch (Exception e) {
            Map<String, Object> response = Map.of(
                "success", false,
                "message", "Error al cambiar contraseña: " + e.getMessage()
            );
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/test-register")
    @Operation(summary = "Test Register", description = "Registra un usuario de prueba para testing")
    public ResponseEntity<?> testRegister() {
        RegisterRequest testUser = new RegisterRequest();
        testUser.setNombre("Usuario");
        testUser.setApellido("Prueba");
        testUser.setEmail("test@stefanini.com");
        testUser.setPassword("test123");
        
        Map<String, Object> result = basicAuthService.registerUser(testUser);
        
        if ((Boolean) result.get("success")) {
            result.put("testMode", true);
            result.put("message", "Usuario de prueba registrado exitosamente");
        }
        
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/test-login")
    @Operation(summary = "Test Login", description = "Login de prueba con usuario test@stefanini.com")
    public ResponseEntity<?> testLogin() {
        LoginRequest testLogin = new LoginRequest();
        testLogin.setEmail("test@stefanini.com");
        testLogin.setPassword("test123");
        
        Map<String, Object> result = basicAuthService.authenticateUser(testLogin);
        
        if ((Boolean) result.get("success")) {
            result.put("testMode", true);
            result.put("message", "Login de prueba exitoso");
        }
        
        return ResponseEntity.ok(result);
    }
}



