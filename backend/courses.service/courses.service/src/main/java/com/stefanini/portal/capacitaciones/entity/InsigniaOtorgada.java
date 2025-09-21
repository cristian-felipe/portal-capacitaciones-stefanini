package com.stefanini.portal.capacitaciones.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "insignias_otorgadas", schema = "capacitaciones")
public class InsigniaOtorgada {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insignia_id", nullable = false)
    private Insignia insignia;
    
    @Column(name = "fecha_otorgada")
    private LocalDateTime fechaOtorgada = LocalDateTime.now();
    
    // Constructores
    public InsigniaOtorgada() {}
    
    public InsigniaOtorgada(UUID usuarioId, Insignia insignia) {
        this.usuarioId = usuarioId;
        this.insignia = insignia;
        this.fechaOtorgada = LocalDateTime.now();
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
    
    public Insignia getInsignia() {
        return insignia;
    }
    
    public void setInsignia(Insignia insignia) {
        this.insignia = insignia;
    }
    
    public LocalDateTime getFechaOtorgada() {
        return fechaOtorgada;
    }
    
    public void setFechaOtorgada(LocalDateTime fechaOtorgada) {
        this.fechaOtorgada = fechaOtorgada;
    }
}

