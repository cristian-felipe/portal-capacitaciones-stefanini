package com.stefanini.portal.capacitaciones.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class InsigniaOtorgadaDTO {
    
    private Long id;
    private UUID usuarioId;
    private Long insigniaId;
    private LocalDateTime fechaOtorgada;
    private InsigniaDTO insignia;
    
    // Constructores
    public InsigniaOtorgadaDTO() {}
    
    public InsigniaOtorgadaDTO(Long id, UUID usuarioId, Long insigniaId, LocalDateTime fechaOtorgada) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.insigniaId = insigniaId;
        this.fechaOtorgada = fechaOtorgada;
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
    
    public Long getInsigniaId() {
        return insigniaId;
    }
    
    public void setInsigniaId(Long insigniaId) {
        this.insigniaId = insigniaId;
    }
    
    public LocalDateTime getFechaOtorgada() {
        return fechaOtorgada;
    }
    
    public void setFechaOtorgada(LocalDateTime fechaOtorgada) {
        this.fechaOtorgada = fechaOtorgada;
    }
    
    public InsigniaDTO getInsignia() {
        return insignia;
    }
    
    public void setInsignia(InsigniaDTO insignia) {
        this.insignia = insignia;
    }
}

