package com.stefanini.portal.capacitaciones.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "roles", schema = "autenticacion")
public class RolEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @NotBlank
    @Column(name = "nombre", unique = true, nullable = false, length = 50)
    private String nombre;
    
    @Column(name = "descripcion", columnDefinition = "text")
    private String descripcion;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    // Relación muchos a muchos con usuarios
    @OneToMany(mappedBy = "rol", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UsuarioRol> usuariosRoles = new ArrayList<>();
    
    // Constructores
    public RolEntity() {}
    
    public RolEntity(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    
    // Getters y Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
    public void addUsuarioRol(UsuarioRol usuarioRol) {
        usuariosRoles.add(usuarioRol);
        usuarioRol.setRol(this);
    }
    
    public void removeUsuarioRol(UsuarioRol usuarioRol) {
        usuariosRoles.remove(usuarioRol);
        usuarioRol.setRol(null);
    }
    
    @Override
    public String toString() {
        return "RolEntity{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}


