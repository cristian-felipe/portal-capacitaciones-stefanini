package com.stefanini.portal.capacitaciones.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stefanini.portal.capacitaciones.entity.Usuario;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    
    private UUID id;
    private String email;
    private String nombre;
    private String apellido;
    private String nombreCompleto;
    private String rol;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private String proveedorOauth;
    private Map<String, Boolean> permisos;
    
    public UserResponse() {}
    
    public UserResponse(Usuario usuario) {
        this.id = usuario.getId();
        this.email = usuario.getCorreoElectronico();
        this.nombre = usuario.getNombre();
        this.apellido = usuario.getApellido();
        this.nombreCompleto = usuario.getNombreCompleto();
        this.rol = usuario.getRol();
        this.activo = usuario.getActivo();
        this.fechaCreacion = usuario.getFechaCreacion();
        this.proveedorOauth = usuario.getProveedorOauth();
        this.permisos = createUserPermissions(usuario);
    }
    
    public static UserResponse from(Usuario usuario) {
        return new UserResponse(usuario);
    }
    private Map<String, Boolean> createUserPermissions(Usuario usuario) {
        Map<String, Boolean> permissions = new java.util.HashMap<>();
        
        permissions.put("canViewCourses", true);
        permissions.put("canEnrollInCourses", true);
        permissions.put("canViewProfile", true);
        
        if (usuario.isInstructor()) {
            permissions.put("canCreateCourses", true);
            permissions.put("canEditCourses", true);
            permissions.put("canViewStudents", true);
        }
        
        if (usuario.isAdmin()) {
            permissions.put("canManageUsers", true);
            permissions.put("canManageSystem", true);
            permissions.put("canViewReports", true);
            permissions.put("canCreateCourses", true);
            permissions.put("canEditCourses", true);
            permissions.put("canViewStudents", true);
        }
        
        return permissions;
    }
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getApellido() {
        return apellido;
    }
    
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
    
    public String getRol() {
        return rol;
    }
    
    public void setRol(String rol) {
        this.rol = rol;
    }
    
    public Boolean getActivo() {
        return activo;
    }
    
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public String getProveedorOauth() {
        return proveedorOauth;
    }
    
    public void setProveedorOauth(String proveedorOauth) {
        this.proveedorOauth = proveedorOauth;
    }
    
    public Map<String, Boolean> getPermisos() {
        return permisos;
    }
    
    public void setPermisos(Map<String, Boolean> permisos) {
        this.permisos = permisos;
    }
    
    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", rol='" + rol + '\'' +
                ", activo=" + activo +
                '}';
    }
}
