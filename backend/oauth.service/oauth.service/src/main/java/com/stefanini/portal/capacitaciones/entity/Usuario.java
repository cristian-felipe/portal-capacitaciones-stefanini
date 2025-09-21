package com.stefanini.portal.capacitaciones.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "usuarios", schema = "autenticacion")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Email
    @NotBlank
    @Column(name = "correo_electronico", unique = true, nullable = false)
    private String correoElectronico;
    
    @Column(name = "hash_contrasena")
    private String hashContrasena;
    
    @NotBlank
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @NotBlank
    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;
    
    @Column(name = "rol", length = 20)
    private String rol = "usuario";
    
    // Relación muchos a muchos con roles (nueva estructura)
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UsuarioRol> usuariosRoles = new ArrayList<>();
    
    @Column(name = "proveedor_oauth", length = 50)
    private String proveedorOauth;
    
    @Column(name = "id_oauth", length = 255)
    private String idOauth;
    
    @NotNull
    @Column(name = "activo")
    private Boolean activo = true;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    // Constructores
    public Usuario() {}
    
    public Usuario(String correoElectronico, String nombre, String apellido, String rol) {
        this.correoElectronico = correoElectronico;
        this.nombre = nombre;
        this.apellido = apellido;
        this.rol = rol;
    }
    
    // Getters y Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getCorreoElectronico() {
        return correoElectronico;
    }
    
    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }
    
    public String getHashContrasena() {
        return hashContrasena;
    }
    
    public void setHashContrasena(String hashContrasena) {
        this.hashContrasena = hashContrasena;
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
    
    public String getRol() {
        return rol;
    }
    
    public void setRol(String rol) {
        this.rol = rol;
    }
    
    public String getProveedorOauth() {
        return proveedorOauth;
    }
    
    public void setProveedorOauth(String proveedorOauth) {
        this.proveedorOauth = proveedorOauth;
    }
    
    public String getIdOauth() {
        return idOauth;
    }
    
    public void setIdOauth(String idOauth) {
        this.idOauth = idOauth;
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
    
    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
    
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
    
    public List<UsuarioRol> getUsuariosRoles() {
        return usuariosRoles;
    }
    
    public void setUsuariosRoles(List<UsuarioRol> usuariosRoles) {
        this.usuariosRoles = usuariosRoles;
    }
    
    // Métodos de utilidad
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
    
    public boolean isAdmin() {
        return "admin".equals(rol);
    }
    
    public boolean isInstructor() {
        return "instructor".equals(rol);
    }
    
    public boolean isUsuario() {
        return "usuario".equals(rol);
    }
    
    // Métodos para manejar la nueva estructura de roles
    public void addRol(RolEntity rolEntity) {
        UsuarioRol usuarioRol = new UsuarioRol(this, rolEntity);
        usuariosRoles.add(usuarioRol);
    }
    
    public void removeRol(RolEntity rolEntity) {
        usuariosRoles.removeIf(ur -> ur.getRol().getId().equals(rolEntity.getId()));
    }
    
    public boolean hasRol(String nombreRol) {
        return usuariosRoles.stream()
                .anyMatch(ur -> ur.getRol().getNombre().equals(nombreRol));
    }
    
    public List<String> getRolesNombres() {
        return usuariosRoles.stream()
                .map(ur -> ur.getRol().getNombre())
                .toList();
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", correoElectronico='" + correoElectronico + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", rol=" + rol +
                ", activo=" + activo +
                '}';
    }
}
