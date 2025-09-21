package com.stefanini.portal.capacitaciones.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProgresoDTO {
    
    private Long id;
    private UUID usuarioId;
    private Long leccionId;
    private String estado;
    private Double porcentaje;
    private LocalDateTime fechaActualizacion;
    private String tituloLeccion;
    private String tituloPrograma;
    
    // Constructores
    public ProgresoDTO() {}
    
    public ProgresoDTO(Long id, UUID usuarioId, Long leccionId, String estado, 
                      Double porcentaje, LocalDateTime fechaActualizacion) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.leccionId = leccionId;
        this.estado = estado;
        this.porcentaje = porcentaje;
        this.fechaActualizacion = fechaActualizacion;
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
    
    public Long getLeccionId() {
        return leccionId;
    }
    
    public void setLeccionId(Long leccionId) {
        this.leccionId = leccionId;
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
    
    public String getTituloLeccion() {
        return tituloLeccion;
    }
    
    public void setTituloLeccion(String tituloLeccion) {
        this.tituloLeccion = tituloLeccion;
    }
    
    public String getTituloPrograma() {
        return tituloPrograma;
    }
    
    public void setTituloPrograma(String tituloPrograma) {
        this.tituloPrograma = tituloPrograma;
    }
}

