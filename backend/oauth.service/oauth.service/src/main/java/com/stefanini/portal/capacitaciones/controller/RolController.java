package com.stefanini.portal.capacitaciones.controller;

import com.stefanini.portal.capacitaciones.entity.RolEntity;
import com.stefanini.portal.capacitaciones.entity.Usuario;
import com.stefanini.portal.capacitaciones.service.RolService;
import com.stefanini.portal.capacitaciones.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "Gestión de Roles", description = "API para gestionar roles del sistema")
public class RolController {
    
    @Autowired
    private RolService rolService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping
    @Operation(summary = "Obtener todos los roles", description = "Retorna la lista de todos los roles del sistema")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RolEntity>> obtenerTodosLosRoles() {
        try {
            List<RolEntity> roles = rolService.obtenerTodosLosRoles();
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener rol por ID", description = "Retorna un rol específico por su ID")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RolEntity> obtenerRolPorId(
            @Parameter(description = "ID del rol") @PathVariable UUID id) {
        try {
            return rolService.buscarPorId(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Obtener rol por nombre", description = "Retorna un rol específico por su nombre")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RolEntity> obtenerRolPorNombre(
            @Parameter(description = "Nombre del rol") @PathVariable String nombre) {
        try {
            return rolService.buscarPorNombre(nombre)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping
    @Operation(summary = "Crear nuevo rol", description = "Crea un nuevo rol en el sistema")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> crearRol(@RequestBody Map<String, String> request) {
        try {
            String nombre = request.get("nombre");
            String descripcion = request.get("descripcion");
            
            if (nombre == null || nombre.trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El nombre del rol es requerido");
                return ResponseEntity.badRequest().body(error);
            }
            
            RolEntity rol = rolService.crearRol(nombre, descripcion);
            return ResponseEntity.status(HttpStatus.CREATED).body(rol);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar rol", description = "Actualiza un rol existente")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarRol(
            @Parameter(description = "ID del rol") @PathVariable UUID id,
            @RequestBody Map<String, String> request) {
        try {
            String nombre = request.get("nombre");
            String descripcion = request.get("descripcion");
            
            if (nombre == null || nombre.trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El nombre del rol es requerido");
                return ResponseEntity.badRequest().body(error);
            }
            
            RolEntity rol = rolService.actualizarRol(id, nombre, descripcion);
            return ResponseEntity.ok(rol);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar rol", description = "Elimina un rol del sistema")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminarRol(
            @Parameter(description = "ID del rol") @PathVariable UUID id) {
        try {
            rolService.eliminarRol(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Rol eliminado exitosamente");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @PostMapping("/{rolId}/usuarios/{usuarioId}")
    @Operation(summary = "Asignar rol a usuario", description = "Asigna un rol específico a un usuario")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> asignarRolAUsuario(
            @Parameter(description = "ID del rol") @PathVariable UUID rolId,
            @Parameter(description = "ID del usuario") @PathVariable UUID usuarioId) {
        try {
            Usuario usuario = usuarioService.buscarPorId(usuarioId)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            
            RolEntity rol = rolService.buscarPorId(rolId)
                    .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));
            
            rolService.asignarRolAUsuario(usuario, rol);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Rol asignado exitosamente al usuario");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @DeleteMapping("/{rolId}/usuarios/{usuarioId}")
    @Operation(summary = "Remover rol de usuario", description = "Remueve un rol específico de un usuario")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removerRolDeUsuario(
            @Parameter(description = "ID del rol") @PathVariable UUID rolId,
            @Parameter(description = "ID del usuario") @PathVariable UUID usuarioId) {
        try {
            Usuario usuario = usuarioService.buscarPorId(usuarioId)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            
            RolEntity rol = rolService.buscarPorId(rolId)
                    .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));
            
            rolService.removerRolDeUsuario(usuario, rol);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Rol removido exitosamente del usuario");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @GetMapping("/usuarios/{usuarioId}")
    @Operation(summary = "Obtener roles de usuario", description = "Retorna todos los roles asignados a un usuario")
    @PreAuthorize("hasRole('ADMIN') or @usuarioService.buscarPorId(#usuarioId).get().getCorreoElectronico() == authentication.name")
    public ResponseEntity<?> obtenerRolesDeUsuario(
            @Parameter(description = "ID del usuario") @PathVariable UUID usuarioId) {
        try {
            var roles = rolService.obtenerRolesDeUsuario(usuarioId);
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @PostMapping("/inicializar")
    @Operation(summary = "Inicializar roles básicos", description = "Crea los roles básicos del sistema")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> inicializarRolesBasicos() {
        try {
            rolService.inicializarRolesBasicos();
            Map<String, String> response = new HashMap<>();
            response.put("message", "Roles básicos inicializados exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}


