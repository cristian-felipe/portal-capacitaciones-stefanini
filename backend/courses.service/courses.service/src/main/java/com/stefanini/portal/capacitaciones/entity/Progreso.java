package com.stefanini.portal.capacitaciones.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "progreso", schema = "capacitaciones")
public class Progreso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leccion_id", nullable = false)
    private Leccion leccion;
    
    @Column(name = "estado", length = 20)
    private String estado = "iniciado";
    
    @Column(name = "porcentaje")
    private Double porcentaje = 0.0;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion = LocalDateTime.now();
    
    // Constructores
    public Progreso() {}
    
    public Progreso(UUID usuarioId, Leccion leccion, String estado, Double porcentaje) {
        this.usuarioId = usuarioId;
        this.leccion = leccion;
        this.estado = estado;
        this.porcentaje = porcentaje;
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public UUID getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(UUID usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    public Leccion getLeccion() {
        return leccion;
    }
    
    public void setLeccion(Leccion leccion) {
        this.leccion = leccion;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public Double getPorcentaje() {
        return porcentaje;
    }
    
    public void setPorcentaje(Double porcentaje) {
        this.porcentaje = porcentaje;
    }
    
    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
    
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}
