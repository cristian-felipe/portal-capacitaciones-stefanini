package com.stefanini.portal.capacitaciones.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class UsuarioRolId implements Serializable {
    
    @Column(name = "usuario_id")
    private UUID usuarioId;
    
    @Column(name = "rol_id")
    private UUID rolId;
    
    // Constructores
    public UsuarioRolId() {}
    
    public UsuarioRolId(UUID usuarioId, UUID rolId) {
        this.usuarioId = usuarioId;
        this.rolId = rolId;
    }
    
    // Getters y Setters
    public UUID getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(UUID usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    public UUID getRolId() {
        return rolId;
    }
    
    public void setRolId(UUID rolId) {
        this.rolId = rolId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioRolId that = (UsuarioRolId) o;
        return Objects.equals(usuarioId, that.usuarioId) &&
               Objects.equals(rolId, that.rolId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(usuarioId, rolId);
    }
    
    @Override
    public String toString() {
        return "UsuarioRolId{" +
                "usuarioId=" + usuarioId +
                ", rolId=" + rolId +
                '}';
    }
}


