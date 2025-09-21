package com.stefanini.portal.capacitaciones.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios_roles", schema = "autenticacion")
public class UsuarioRol {
    
    @EmbeddedId
    private UsuarioRolId id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("usuarioId")
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("rolId")
    @JoinColumn(name = "rol_id")
    private RolEntity rol;
    
    @CreationTimestamp
    @Column(name = "fecha_asignacion")
    private LocalDateTime fechaAsignacion;
    
    // Constructores
    public UsuarioRol() {
        this.id = new UsuarioRolId();
    }
    
    public UsuarioRol(Usuario usuario, RolEntity rol) {
        this.id = new UsuarioRolId(usuario.getId(), rol.getId());
        this.usuario = usuario;
        this.rol = rol;
    }
    
    // Getters y Setters
    public UsuarioRolId getId() {
        return id;
    }
    
    public void setId(UsuarioRolId id) {
        this.id = id;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        if (id == null) {
            id = new UsuarioRolId();
        }
        id.setUsuarioId(usuario.getId());
    }
    
    public RolEntity getRol() {
        return rol;
    }
    
    public void setRol(RolEntity rol) {
        this.rol = rol;
        if (id == null) {
            id = new UsuarioRolId();
        }
        id.setRolId(rol.getId());
    }
    
    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }
    
    public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }
    
    @Override
    public String toString() {
        return "UsuarioRol{" +
                "usuarioId=" + (id != null ? id.getUsuarioId() : null) +
                ", rolId=" + (id != null ? id.getRolId() : null) +
                ", fechaAsignacion=" + fechaAsignacion +
                '}';
    }
}
